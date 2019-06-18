package gateway.command.serialize;

import com.fasterxml.jackson.databind.ObjectMapper;
import gateway.command.event.kafka.EventEnvelope;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;


public class JsonDeserializer<T> implements Deserializer<T> {
    private final ObjectMapper mapper;
    private final Class<T> clazz;

    public JsonDeserializer() {
        this.mapper = new ObjectMapper();
        this.mapper.findAndRegisterModules();
        this.clazz = (Class<T>) EventEnvelope.class;
    }

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {

    }

    @Override
    public T deserialize(String topic, byte[] data) {
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public T deserialize(String data) {
        try {
            return mapper.readValue(data, clazz);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void close() {

    }
}
