package hotel.write.event.listeners;


import hotel.write.commands.Command;
import hotel.write.commands.HotelDeleteCommand;
import hotel.write.commands.HotelSaveCommand;
import hotel.write.commands.HotelUpdateCommand;
import hotel.write.kafka.EventEnvelope;
import hotel.write.services.write.HotelService;
import io.micronaut.configuration.kafka.ConsumerAware;
import io.micronaut.configuration.kafka.annotation.KafkaKey;
import io.micronaut.configuration.kafka.annotation.KafkaListener;
import io.micronaut.configuration.kafka.annotation.Topic;
import io.micronaut.http.MediaType;
import io.micronaut.http.codec.MediaTypeCodecRegistry;
import io.micronaut.jackson.codec.JsonMediaTypeCodec;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRebalanceListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.common.TopicPartition;

import javax.annotation.Nonnull;
import javax.annotation.concurrent.ThreadSafe;
import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.*;

@ThreadSafe
@KafkaListener
public class KafkaEventListener implements ConsumerRebalanceListener, ConsumerAware {

    Map<String, Class> commandClasses = new HashMap<String,Class>() {
        {
            put(EventEnvelope.class.getSimpleName(), EventEnvelope.class);
            put(HotelSaveCommand.class.getSimpleName(), HotelSaveCommand.class);
            put(HotelUpdateCommand.class.getSimpleName(), HotelUpdateCommand.class);
            put(HotelDeleteCommand.class.getSimpleName(), HotelDeleteCommand.class);
        }
    };
    @Inject
    protected MediaTypeCodecRegistry mediaTypeCodecRegistry;


   // @GuardedBy("kafkaConsumers")
  //  private final Set<Consumer> kafkaConsumers = new HashSet<>();

    private Consumer consumer;

    @Override
    public void setKafkaConsumer(@Nonnull final Consumer consumer) {
        this.consumer=consumer;
       // synchronized (kafkaConsumers) {
       //     this.kafkaConsumers.add(consumer);
        //}
    }

    //protected static final Logger LOG = LoggerFactory.getLogger(KafkaEventListener.class);

    @Inject
    private HotelService dao;
    //private QueryHotelViewDao dao;

    @Topic("hotel")
    public void consume(@KafkaKey String hotelCode,  String hotelCreatedEvent) {
        if (hotelCode.contains("_")) {
            String eventType = hotelCode.split("_")[0];
            if (hotelCode!=null) {
                // LOG.debug("KAKFA EVENT RECEIVED AT CUSTOM APPLICATION LISTENER hotelCreated "+hotelCode);
                //System.out.println("READ --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  hotelCreated ---"+hotelCreatedEvent.getDtoFromEvent()+" "+hotelCode);
                System.out.println("_____> WRITE --------------- KAKFA hotelCreated EVENT RECEIVED AT CUSTOM APPLICATION LISTENER  --- "+hotelCode+ " -- event "+hotelCreatedEvent);

                JsonMediaTypeCodec mediaTypeCodec = (JsonMediaTypeCodec) mediaTypeCodecRegistry.findCodec(MediaType.APPLICATION_JSON_TYPE)
                        .orElseThrow(() -> new IllegalStateException("No JSON codec found"));

                System.out.println(eventType+" is current eventType ");
                Command cmd = (Command) mediaTypeCodec.decode(commandClasses.get(eventType),hotelCreatedEvent);
                System.out.println(" command "+cmd);

                //eventPublisher1.publish(embeddedServer,topic,cmd);
                //Hotel hotel = args.getHotel();
                //hotelWriteClient.save(hotel);
                //EventEnvelope cmd =  hotelCreatedEvent.getDtoFromEvent();
                // System.out.println(" command "+cmd);
                //eventPublisher1.publish(embeddedServer,topic,cmd);
                //Hotel hotel = args.getHotel();
                //hotelWriteClient.save(hotel);

                //EventEnvelope cmd =  hotelCreatedEvent.getDtoFromEvent();
                System.out.println("Default save of hotel in gateway ---------------- command "+cmd);
                if (hotelCreatedEvent !=null ) {
                    final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


                final Set<ConstraintViolation<Command>> constraintViolations = validator.validate(cmd);
                if (constraintViolations.size() > 0) {
                    Set<String> violationMessages = new HashSet<String>();

                    for (ConstraintViolation<?> constraintViolation : constraintViolations) {
                        violationMessages.add(constraintViolation.getMessage());
                        //violationMessages.add(constraintViolation.getPropertyPath() + ": " + constraintViolation.getMessage());
                    }
                    System.out.println(" HOTEL-WRITE VALIDATION ERROR - COMMAND BUS FAILED VALIDATION::: 01 ---->"+violationMessages);
//            throw new ValidationException("Hotel is not valid:\n" + violationMessages);

                    //TODO - We need to websocket back and pickup
                    /// return HttpResponse.badRequest(violationMessages);
                } else {

                    //Hotel h= cmd.createHotel();
                    //if (h !=null ) {
                    //  dao.save(h);
                    //}

                    System.out.println(" HOTEL-WRITE ALL good ---->"+cmd.getClass());
                    if (cmd instanceof HotelSaveCommand) {
                        System.out.println("Saving item sending to  hotel");
                        dao.save((HotelSaveCommand) cmd);
                    }


                }

                }

            }

        }

    }


    @Override
    public void onPartitionsRevoked(Collection<TopicPartition> partitions) {
        System.out.println("onPartitionsRevoked------------------------------------------------------------------------------------------------");
        // partitions.iterator().forEachRemaining();
        // save offsets here


        for(TopicPartition partition: partitions) {
            synchronized (partition) {
                // kafkaConsumers.forEach(consumer -> {
                //synchronized (kafkaConsumers) {
                System.out.println("  onPartitionsRevoked parition : " + partition + ' ');
                // + consumer.position(partition));
                //consumer.seek(partition,1);
                // }
                // });
            }
            //   saveOffsetInExternalStore(consumer.position(partition));
        }
    }


    /**
     * This triggers a new node to build h2 db up based on existing received kafka events
     * @param partitions
     */
    @Override
    public void onPartitionsAssigned(Collection<TopicPartition> partitions) {
        for (TopicPartition partition : partitions) {
            System.out.println("onPartitionsAssigned  Topic " + partition.topic() + " polling");
            synchronized (consumer) {
                //this.consumer.
                this.consumer.subscribe(Arrays.asList(partition.topic()));
            }
            ConsumerRecords<String, String> records = this.consumer.poll(100);
            try {
                System.out.println(" Topic " + partition.topic() + " seekBegin");
                this.consumer.seek(partition,1);
            } catch (Exception e) {
                rewind(records);
                //Thread.sleep(100);
            }
            for (ConsumerRecord<String, String> record : records)
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());

            System.out.println("Topics done - subscribing: ");
        }
    }
    private void rewind(ConsumerRecords<String, String> records) {
        records.partitions().forEach(partition -> {
            long offset = records.records(partition).get(0).offset();
            consumer.seek(partition, offset);
        });
    }

}