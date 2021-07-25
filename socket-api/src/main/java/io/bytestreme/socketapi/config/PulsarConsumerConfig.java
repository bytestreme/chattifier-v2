package io.bytestreme.socketapi.config;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.PulsarUtil;
import io.bytestreme.data.pulsar.event.output.PulsarMessageOutputEvent;
import io.bytestreme.socketapi.service.pulsar.MessageOutHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Consumer;
import org.apache.pulsar.client.api.PulsarClient;
import org.apache.pulsar.client.api.Schema;
import org.apache.pulsar.client.api.SubscriptionType;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class PulsarConsumerConfig {

    @Value("${pulsar.topicRealTime}")
    private String topicRealTime;

    @Autowired
    private MessageOutHandler messageOutHandler;

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Consumer<PulsarMessageOutputEvent> messageOutConsumer(PulsarClient client) {
        SchemaDefinition<PulsarMessageOutputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageOutputEvent>builder()
                .withPojo(PulsarMessageOutputEvent.class)
                .build();
        return client
                .<PulsarMessageOutputEvent>newConsumer(Schema.JSON(schemaDefinition))
                .topic(topicRealTime)
                .consumerName(PulsarUtil.buildRandomNameForId(PulsarTypeCodes.OutputEventType.MESSAGE_OUT))
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName(UUID.randomUUID().toString())
                .messageListener((consumer, message) -> messageOutHandler.handle(message.getValue()))
                .subscribe();
    }

}
