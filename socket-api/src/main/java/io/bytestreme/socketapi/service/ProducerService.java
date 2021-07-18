package io.bytestreme.socketapi.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.stream.IntStream;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final Producer<byte[]> producer;

    @PostConstruct
    public void init() {
        IntStream.range(0, 150).forEach(i -> {
            String content = String.format("hi-pulsar-%d", i);
            try {
                log.info("producer sent: " + content);
                producer.send(content.getBytes());
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        });
    }
}
