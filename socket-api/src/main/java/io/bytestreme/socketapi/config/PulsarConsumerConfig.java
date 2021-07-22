package io.bytestreme.socketapi.config;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.event.PulsarMessageOutEvent;
import io.bytestreme.socketapi.service.PulsarEventSink;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Slf4j
@Configuration
public class PulsarConsumerConfig {

    @Value("${pulsar.topicRealTime}")
    private String topicRealTime;

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Consumer<PulsarMessageOutEvent> messageOutProducer(PulsarClient client, PulsarEventSink pulsarEventSink) {
        SchemaDefinition<PulsarMessageOutEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageOutEvent>builder()
                .withPojo(PulsarMessageOutEvent.class)
                .build();
        return client
                .<PulsarMessageOutEvent>newConsumer(Schema.JSON(schemaDefinition))
                .topic(topicRealTime)
                .consumerName(UUID.randomUUID() + "__" + PulsarTypeCodes.OutputEventType.MESSAGE_OUT)
                .subscriptionType(SubscriptionType.Shared)
                .subscriptionName(UUID.randomUUID().toString())
                .messageListener((MessageListener<PulsarMessageOutEvent>) (consumer, message) -> {
                    log.info("listener PulsarMessageOutEvent ");
                    pulsarEventSink.onNext(message.getValue());
                })
                .subscribe();
    }

}
