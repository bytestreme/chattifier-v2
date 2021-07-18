package io.bytestreme.socketapi.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Slf4j
@Configuration
public class PulsarConfig {

    private static final String SERVICE_URL = "pulsar+ssl://pulsar-gcp-useast4.streaming.datastax.com:6651";

    @SneakyThrows
    @Scope("prototype")
    @Bean(destroyMethod = "close")
    public PulsarClient pulsarClient() {
        log.info("instantiated client");
        return PulsarClient.builder()
                .serviceUrl(SERVICE_URL)
                .authentication(
                        AuthenticationFactory.token("eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJjbGllbnQ7OTQyZWJiNTktNDQxNC00MGNjLWE0NTktYmJkMjgyN2NhNTVkO2RHNXVkQT09In0.twaKQ-Q6RSH-nIjrgC9Uqpsb02u_v0uYSqoxctASEvJm2MC__9B3u7GSn9X_URQQeBIPtQyKMyR_fOr8WLQXlWI9-QH-sfRSsvtQ3BqLOSh9czOJEWhUXekmcFXG8NK3E6RRB4pQtpSgOi9qMt2kL-Kox2fdbtLmsTuAB0DvjoIp_liF10ST9Pja_aRRO9WS_tCJYwv-rQI4IDB40gZPeC6_qW0uVogh_dqnZtIQ7HnR_2kSNfWkvncDaes6IdN-ehL2phm8bwz8VBxmbmAadN-nZmt9553nRXERcjFuKhSdEPqJCJ9-p4qKq81dvEtbedOaLvl22irvOd-7USsYWA")
                )
                .build();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Producer<byte[]> producer(PulsarClient client) {
        return client.newProducer()
                .topic("persistent://tnnt/default/my-topic")
                .create();
    }

    @SneakyThrows
    @Bean(destroyMethod = "close")
    public Consumer<byte[]> consumer(PulsarClient client) {
        MessageListener<byte[]> messageListener = (consumer, msg) -> {
            try {
                log.info(this.hashCode() + " consumer msg: " + new String(msg.getData()));
                consumer.acknowledge(msg);
            } catch (Exception e) {
                consumer.negativeAcknowledge(msg);
            }
        };

        return client.newConsumer()
                .topic("tnnt/default/my-topic")
                .subscriptionName("my-subscription")
                .subscriptionType(SubscriptionType.Shared)
                .messageListener(messageListener)
                .subscribe();
    }

}
