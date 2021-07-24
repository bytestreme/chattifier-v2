package io.bytestreme.socketapi.config;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.PulsarUtil;
import io.bytestreme.data.pulsar.event.PulsarMessageEditInputEvent;
import io.bytestreme.data.pulsar.event.PulsarMessageInputEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

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
                .producerName(PulsarUtil.buildRandomNameForId(PulsarTypeCodes.InputEventType.MESSAGE_IN))
                .create();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Producer<PulsarMessageEditInputEvent> messageEditProducer(PulsarClient client) {
        SchemaDefinition<PulsarMessageEditInputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageEditInputEvent>builder()
                .withPojo(PulsarMessageEditInputEvent.class)
                .build();
        return client
                .<PulsarMessageEditInputEvent>newProducer(Schema.JSON(schemaDefinition))
                .topic(topic)
                .producerName(PulsarUtil.buildRandomNameForId(PulsarTypeCodes.InputEventType.MESSAGE_EDIT))
                .create();
    }
}
