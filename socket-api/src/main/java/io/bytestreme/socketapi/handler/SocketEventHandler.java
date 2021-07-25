package io.bytestreme.socketapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.event.output.PulsarMessageOutputEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketEventOutput;
import io.bytestreme.socketapi.service.ProducerService;
import io.bytestreme.socketapi.service.PulsarEventSink;
import io.bytestreme.socketapi.service.WebSocketMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;
import java.time.Duration;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketEventHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketMessageMapper mapper;
    private final ProducerService producerService;
    private final PulsarEventSink pulsarEventSink;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Value("${token.forwarded.user-id}")
    private String USER_ID_HEADER;

    @Value("${redis.key.connected-users}")
    private String REDIS_CONNECTED_USERS;

    @Override
    @Nonnull
    public Mono<Void> handle(@Nonnull WebSocketSession session) {
        String userId = Optional.ofNullable(session.getHandshakeInfo().getHeaders().getFirst(USER_ID_HEADER))
                .orElse(UUID.randomUUID().toString());
        log.info("SocketEventHandler::handle => header " + USER_ID_HEADER);
        log.info("SocketEventHandler::handle => chatId " + userId);
        return session.receive()
                .map(WebSocketMessage::retain)
                .map(WebSocketMessage::getPayload)
                .publishOn(Schedulers.boundedElastic())
                .transform(mapper::decode)
                .transform(input -> this.handle(input, userId))
                .onBackpressureBuffer()
                .transform(m -> mapper.encode(m, session.bufferFactory()))
                .map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db))
                .as(session::send)
                .doOnSubscribe(s -> {
                    log.info("handler => user subscribed " + userId);
                    redisTemplate.opsForSet().add(REDIS_CONNECTED_USERS, userId)
                            .doOnNext((rd) -> log.info("redis => added connected user " + userId))
                            .subscribe();
                })
                .doOnTerminate(() -> {
                    log.info("handler => user disconnected " + userId);
                    redisTemplate.opsForSet().remove(REDIS_CONNECTED_USERS, userId)
                            .doOnNext(rd2 -> log.info("redis => removed user " + userId))
                            .subscribe();
                });
    }

    private Flux<SocketEventOutput> handle(Flux<SocketEventInput> inputFlux, String userId) {
        Flux<SocketEventOutput> events = pulsarEventSink
                .eventStream(UUID.fromString(userId))
                .map(x -> {
                    log.info("mapping Flux<SocketEventOutput>");
                    SocketEventOutput eventOutput = new SocketEventOutput();
                    var pulsarEvent = (PulsarMessageOutputEvent) x;
                    try {
                        String jsonOut = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(pulsarEvent);
                        eventOutput.setData(jsonOut.getBytes());
                        eventOutput.setType(PulsarTypeCodes.OutputEventType.MESSAGE_OUT);
                        return eventOutput;
                    } catch (JsonProcessingException e) {
                        return new SocketEventOutput("error".getBytes(), PulsarTypeCodes.OutputEventType.NOK);
                    }
                })
                .doOnNext(x -> log.info(new String(x.getData())))
                .log("event from eventStream");
        return Flux.merge(
                Flux.interval(Duration.ofSeconds(5))
                        .map(
                                x -> new SocketEventOutput("no data".getBytes(), PulsarTypeCodes.OutputEventType.OK)
                        ),
                producerService.produceSocketEvent(inputFlux),
                events
        );
    }

}
