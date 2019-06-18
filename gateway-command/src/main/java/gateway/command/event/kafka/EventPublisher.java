package gateway.command.event.kafka;

import gateway.command.event.commands.Command;
import io.micronaut.runtime.server.EmbeddedServer;

public  interface EventPublisher {
    <T extends Command> void publish(EmbeddedServer embeddedServer, String topic, T command);
    <T extends Command> String serializeCommand(T command);
}