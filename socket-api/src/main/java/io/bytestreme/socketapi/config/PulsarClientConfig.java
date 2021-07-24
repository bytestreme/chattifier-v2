package io.bytestreme.socketapi.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.AuthenticationFactory;
import org.apache.pulsar.client.api.PulsarClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class PulsarClientConfig {

    @Value("${pulsar.service-url}")
    private String serviceUrl;

    @Value("${pulsar.authentication}")
    private String authentication;

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

}
