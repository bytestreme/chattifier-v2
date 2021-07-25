package io.bytestreme.socketapi.service.pulsar;

import io.bytestreme.data.pulsar.event.input.AbstractPulsarEvent;

public interface OutputEventHandler<T extends AbstractPulsarEvent> {
    void handle(T abstractPulsarEvent);
}
