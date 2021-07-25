package io.bytestreme.messageservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.autoconfigure.security.reactive.ReactiveManagementWebSecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CassandraAutoConfiguration;
import org.springframework.boot.autoconfigure.security.reactive.ReactiveSecurityAutoConfiguration;

@SpringBootApplication(exclude = {
        ReactiveSecurityAutoConfiguration.class,
        ReactiveManagementWebSecurityAutoConfiguration.class,
        CassandraAutoConfiguration.class
})
public class MessageService {

    public static void main(String[] args) {
        SpringApplication.run(MessageService.class, args);
    }

}
