package io.bytestreme.socketapi.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.data.pulsar.event.PulsarMessageOutEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketEventOutput;
import io.bytestreme.socketapi.service.ProducerService;
import io.bytestreme.socketapi.service.PulsarEventSink;
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
import java.time.Duration;
import java.util.Base64;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class SocketEventHandler implements WebSocketHandler {

    private final ObjectMapper objectMapper;
    private final WebSocketMessageMapper mapper;
    private final ProducerService producerService;
    private final PulsarEventSink pulsarEventSink;

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
        Flux<SocketEventOutput> events = pulsarEventSink
                .eventStream(UUID.fromString("b9792eab-1ed3-4834-8329-277b6109a7b9"))
                .map(x -> {
                    log.info("mapping Flux<SocketEventOutput>");
                    SocketEventOutput eventOutput = new SocketEventOutput();
                    var pulsarEvent = (PulsarMessageOutEvent) x;
                    try {
                        String jsonOut = objectMapper.writerWithDefaultPrettyPrinter()
                                .writeValueAsString(pulsarEvent);
                        eventOutput.setData(Base64.getEncoder().encode(jsonOut.getBytes()));
                        eventOutput.setType(PulsarTypeCodes.OutputEventType.MESSAGE_OUT);
                        return eventOutput;
                    } catch (JsonProcessingException e) {
                        return new SocketEventOutput("error".getBytes(), PulsarTypeCodes.OutputEventType.NOK);
                    }
                })
                .doOnNext(x-> log.info(new String(x.getData())))
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
