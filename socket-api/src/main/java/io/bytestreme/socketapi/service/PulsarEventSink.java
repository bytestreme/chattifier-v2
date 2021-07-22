package io.bytestreme.socketapi.service;

import io.bytestreme.data.pulsar.AbstractPulsarOutputEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class PulsarEventSink {

    private final Sinks.Many<AbstractPulsarOutputEvent> eventSink = Sinks.many().multicast().onBackpressureBuffer();

    public void onNext(AbstractPulsarOutputEvent event) {
        log.info("trying to emit...");
        eventSink.tryEmitNext(event);
    }

    public Flux<AbstractPulsarOutputEvent> eventStream(UUID user) {
        return eventSink.asFlux()
                .filter(x -> x.getTarget().equals(user))
                .log("logging log()");
    }

}
