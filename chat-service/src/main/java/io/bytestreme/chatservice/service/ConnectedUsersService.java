package io.bytestreme.chatservice.service;

import io.bytestreme.data.pulsar.event.output.PulsarMessageOutputEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;

import java.util.UUID;
import java.util.function.BiConsumer;

@Service
@Slf4j
@RequiredArgsConstructor
public class ConnectedUsersService {

    @Value("${redis.key.connected-users}")
    private String REDIS_CONNECTED_USERS;

    private final ReactiveRedisTemplate<String, String> redisTemplate;

    public Mono<PulsarMessageOutputEvent> isConnected(PulsarMessageOutputEvent event) {
        log.info("ConnectedUsersService::isUserConnected => uuid: " + event.getTarget());
        return redisTemplate
                .opsForSet()
                .isMember(REDIS_CONNECTED_USERS, event.getTarget().toString())
                .handle((aBoolean, sink) -> {
                    if (aBoolean) {
                        sink.next(event);
                    }
                });
    }

}
