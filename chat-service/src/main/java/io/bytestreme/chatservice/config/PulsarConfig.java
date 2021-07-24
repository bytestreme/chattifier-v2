package io.bytestreme.chatservice.config;

import io.bytestreme.chatservice.service.MessageInputService;
import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.event.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.PulsarMessageOutEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.UUID;

@Slf4j
@Configuration
public class PulsarConfig {

    private static final String SOCKET_CONSUMER_PREFIX = "socket_consumer_";

    @Value("${pulsar.service-url}")
    private String serviceUrl;

    @Value("${pulsar.authentication}")
    private String authentication;

    @Value("${pulsar.topic}")
    private String topic;

    @Value("${pulsar.topicRealTime}")
    private String topicRealTime;

    @Autowired
    private MessageInputService messageInputService;

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public PulsarClient pulsarClient() {
        return PulsarClient.builder()
                .serviceUrl(serviceUrl)
                .authentication(
                        AuthenticationFactory
                                .token(authentication)
                )
                .build();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Producer<PulsarMessageOutEvent> messageOutEventProducer(PulsarClient client) {
        SchemaDefinition<PulsarMessageOutEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageOutEvent>builder()
                .withPojo(PulsarMessageOutEvent.class)
                .build();
        return client
                .<PulsarMessageOutEvent>newProducer(Schema.JSON(schemaDefinition))
                .topic(topicRealTime)
                .producerName(UUID.randomUUID() + "__" + PulsarTypeCodes.OutputEventType.MESSAGE_OUT)
                .create();
    }


    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Consumer<PulsarMessageInputEvent> consumer(PulsarClient client) {

        SchemaDefinition<PulsarMessageInputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageInputEvent>builder()
                .withPojo(PulsarMessageInputEvent.class)
                .build();

        return client.<PulsarMessageInputEvent>newConsumer(Schema.JSON(schemaDefinition))
                .topic(topic)
                .subscriptionName(SOCKET_CONSUMER_PREFIX + UUID.randomUUID())
                .subscriptionType(SubscriptionType.Shared)
                .consumerName(UUID.randomUUID() + "__" + PulsarTypeCodes.InputEventType.MESSAGE_IN)
                .messageListener(messageInputService.handleMessages())
                .subscribe();
    }
}
