package io.bytestreme.socketapi.config;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageEditEvent;
import io.bytestreme.socketapi.data.pulsar.PulsarMessageInputEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PulsarProducerConfig {

    @Value("${pulsar.topic}")
    private String topic;

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Producer<PulsarMessageInputEvent> messageInputProducer(PulsarClient client) {
        SchemaDefinition<PulsarMessageInputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageInputEvent>builder()
                .withPojo(PulsarMessageInputEvent.class)
                .build();
        return client
                .<PulsarMessageInputEvent>newProducer(Schema.JSON(schemaDefinition))
                .topic(topic)
                .producerName(String.valueOf(SocketEventInput.InputEventType.MESSAGE_IN))
                .create();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Producer<PulsarMessageEditEvent> messageEditProducer(PulsarClient client) {
        SchemaDefinition<PulsarMessageEditEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageEditEvent>builder()
                .withPojo(PulsarMessageEditEvent.class)
                .build();
        return client
                .<PulsarMessageEditEvent>newProducer(Schema.JSON(schemaDefinition))
                .topic(topic)
                .producerName(String.valueOf(SocketEventInput.InputEventType.MESSAGE_EDIT))
                .create();
    }
}
