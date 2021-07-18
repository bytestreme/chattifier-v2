package io.bytestreme.socketapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class SocketApi {

    public static void main(String[] args) {
        SpringApplication.run(SocketApi.class, args);
    }

}
