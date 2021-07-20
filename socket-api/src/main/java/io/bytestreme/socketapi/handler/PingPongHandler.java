package io.bytestreme.socketapi.handler;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketEventOutput;
import io.bytestreme.socketapi.service.ProducerService;
import io.bytestreme.socketapi.service.WebSocketMessageMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import javax.annotation.Nonnull;

@Service
@Slf4j
@RequiredArgsConstructor
public class PingPongHandler implements WebSocketHandler {

    private final WebSocketMessageMapper mapper;
    private final ProducerService producerService;

    @Override
    @Nonnull
    public Mono<Void> handle(@Nonnull WebSocketSession session) {
        return session.receive()
                .map(WebSocketMessage::retain)
                .map(WebSocketMessage::getPayload)
                .publishOn(Schedulers.boundedElastic())
                .transform(mapper::decode)
                .transform(this::handle)
                .onBackpressureBuffer()
                .transform(m -> mapper.encode(m, session.bufferFactory()))
                .map(db -> new WebSocketMessage(WebSocketMessage.Type.TEXT, db))
                .as(session::send);
    }

    private Flux<SocketEventOutput> handle(Flux<SocketEventInput> inputFlux) {
        return producerService.produceSocketEvent(inputFlux);
    }

}
