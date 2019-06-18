package hotel.write.commandHandlers;

import hotel.write.commands.*;
import hotel.write.commands.commandActions.CreateHotelCommand;
import hotel.write.commands.commandActions.DeleteHotelCommand;
import hotel.write.commands.commandActions.UpdateHotelCommand;
import hotel.write.domain.Hotel;
import hotel.write.domain.interfaces.HotelsEventInterface;
import hotel.write.event.*;
import hotel.write.event.HotelSaveCommandEvent;

import hotel.write.kafka.EventPublisher;
import hotel.write.model.*;
import hotel.write.model.Command;
import hotel.write.services.write.Dao;
import io.micronaut.context.annotation.Primary;
import io.micronaut.runtime.server.EmbeddedServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * This calls the EventPublisher - which is kafka client to push a kafka event out.
 *
 */
@Singleton
@Primary
public class CreateHotelHandler extends AbstractCommandHandler<HotelSaveCommand>{

	protected static final Logger LOG = LoggerFactory.getLogger(CreateHotelHandler.class);
	@Inject
	private HotelsEventInterface dao;

	private EmbeddedServer embeddedServer;

	@Inject
	public CreateHotelHandler(EventPublisher publisher,EmbeddedServer embeddedServer) {
		super(publisher,embeddedServer);
        System.out.println("Create Hotel handler");
	}

	@Override
	HotelSaveCommand getDto(Command<HotelSaveCommand> command) {
		HotelSaveCommand hotelSaveCommand = ((HotelSaveCommand) command);
		//Hotel hotel = new Hotel(hotelSaveCommand.getCode(),hotelSaveCommand.getName(), hotelSaveCommand.getPhone(), hotelSaveCommand.getEmail(), hotelSaveCommand.getUpdateUserId());
		//LOG.debug("hotel:" + hotel.getId() + "," + hotel.getName());
        //System.out.println("hotel:" + hotel.getId() + "," + hotel.getName());
		return hotelSaveCommand;
	}


	@Override
	void save(HotelSaveCommand hotelSaveCommand) {
        System.out.println("--- from abstract class save hotel:" + hotelSaveCommand.getCode() + "," + hotelSaveCommand.getName());
		dao.save( new Hotel(hotelSaveCommand.getCode(),hotelSaveCommand.getName(), hotelSaveCommand.getPhone(), hotelSaveCommand.getEmail(), hotelSaveCommand.getUpdateUserId()));
	}


	@Override
    Result<HotelSaveCommand> buildResult(HotelSaveCommand hotel) {
        System.out.println("buildResult HotelSaveCommand:" + hotel.getCode() + "," + hotel.getName());
		return new HotelSaveResult();
	}
}