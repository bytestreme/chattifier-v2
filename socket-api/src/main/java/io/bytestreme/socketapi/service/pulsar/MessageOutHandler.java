package io.bytestreme.socketapi.service.pulsar;

import io.bytestreme.data.pulsar.event.output.PulsarMessageOutputEvent;
import io.bytestreme.socketapi.service.PulsarEventSink;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessageOutHandler implements OutputEventHandler<PulsarMessageOutputEvent> {

    private final PulsarEventSink pulsarEventSink;

    @Override
    public void handle(PulsarMessageOutputEvent abstractPulsarEvent) {
        log.info("MessageOutHandler::handle ");
        pulsarEventSink.emitEvent(abstractPulsarEvent);
    }

}
