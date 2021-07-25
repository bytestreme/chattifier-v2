package io.bytestreme.socketapi.service;

import io.bytestreme.data.pulsar.event.output.AbstractPulsarOutputEvent;
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

    private final Sinks.Many<AbstractPulsarOutputEvent> eventSink = Sinks.many()
            .multicast()
            .onBackpressureBuffer();

    public void emitEvent(AbstractPulsarOutputEvent event) {
        log.info("trying to emit for... " + event.getTarget());
        var attempt = eventSink.tryEmitNext(event);
        log.info("attempt to emit " + attempt.isSuccess());
    }

    public Flux<AbstractPulsarOutputEvent> eventStream(UUID user) {
        log.info("getting for: " + user);
        return eventSink.asFlux()
                .doOnNext(s -> log.info("eventStream::onNext: " + s.getTarget()))
                .filter(x -> x.getTarget().equals(user))
                .doOnSubscribe(s -> log.info("eventStream::onSubscribe"))
                .doOnCancel(() -> log.info("eventStream::onCancel"));
    }

}
