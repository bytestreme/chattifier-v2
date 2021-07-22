package io.bytestreme.chatservice.config;

import io.bytestreme.data.pulsar.event.PulsarMessageInputEvent;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.apache.pulsar.client.api.schema.SchemaDefinition;
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

    @SneakyThrows
    @Scope("prototype")
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
    public Consumer<PulsarMessageInputEvent> consumer(PulsarClient client) {
        MessageListener<PulsarMessageInputEvent> messageListener = (consumer, msg) -> {
            try {
                log.info("consumer msg: " + msg.getValue().getContent());
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
                log.error(e.getMessage());
            }
        };
        SchemaDefinition<PulsarMessageInputEvent> schemaDefinition = SchemaDefinition
                .<PulsarMessageInputEvent>builder()
                .withPojo(PulsarMessageInputEvent.class)
                .build();

        return client.<PulsarMessageInputEvent>newConsumer(Schema.JSON(schemaDefinition))
                .topic(topic)
                .subscriptionName(SOCKET_CONSUMER_PREFIX + UUID.randomUUID())
                .subscriptionType(SubscriptionType.Shared)
                .messageListener(messageListener)
                .subscribe();
    }
}
