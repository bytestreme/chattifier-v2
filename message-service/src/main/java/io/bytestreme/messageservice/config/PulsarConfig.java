package io.bytestreme.messageservice.config;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.event.input.PulsarMessageInputEvent;
import io.bytestreme.data.pulsar.event.input.PulsarMessagePersistRequestInputEvent;
import io.bytestreme.messageservice.service.MessagePersistingService;
import lombok.SneakyThrows;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.UUID;

@Configuration
public class PulsarConfig {

    @Value("${pulsar.service-url}")
    private String serviceUrl;

    @Value("${pulsar.authentication}")
    private String authentication;

    @Value("${pulsar.topic}")
    private String topic;

    @Value("${pulsar.topicRealTime}")
    private String topicRealTime;

    @Value("${pulsar.topicPersistMessages}")
    private String topicPersistMessages;

    private static final String SOCKET_CONSUMER_PREFIX = "socket_consumer_";

    @Autowired
    private MessagePersistingService persistingService;

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
    public Consumer<PulsarMessagePersistRequestInputEvent> consumer(PulsarClient client) {

        SchemaDefinition<PulsarMessagePersistRequestInputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessagePersistRequestInputEvent>builder()
                .withPojo(PulsarMessagePersistRequestInputEvent.class)
                .build();

        return client.<PulsarMessagePersistRequestInputEvent>newConsumer(Schema.JSON(schemaDefinition))
                .topic(topicPersistMessages)
                .subscriptionName(SOCKET_CONSUMER_PREFIX + UUID.randomUUID())
                .subscriptionType(SubscriptionType.Shared)
                .consumerName(UUID.randomUUID() + "__" + PulsarTypeCodes.InputEventType.MESSAGE_IN)
                .messageListener(persistingService.handleMessages())
                .subscribe();
    }
}
