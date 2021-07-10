package io.bytestreme.cloudgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudGateway {

    public static void main(String[] args) {
        SpringApplication.run(CloudGateway.class, args);
    }

}
