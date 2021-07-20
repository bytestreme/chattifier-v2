package io.bytestreme.socketapi.config;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageInputEvent;
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
public class PulsarClientConfig {

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
    public Producer<byte[]> producer(PulsarClient client) {
        return client.newProducer()
                .topic(topic)
                .create();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Consumer<byte[]> consumer(PulsarClient client) {
        MessageListener<byte[]> messageListener = (consumer, msg) -> {
            try {
                log.info("consumer msg: " + new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
                log.error(e.getMessage());
            }
        };

        return client.newConsumer()
                .topic(topic)
                .subscriptionName(SOCKET_CONSUMER_PREFIX + UUID.randomUUID())
                .subscriptionType(SubscriptionType.Shared)
                .messageListener(messageListener)
                .subscribe();
    }

}
