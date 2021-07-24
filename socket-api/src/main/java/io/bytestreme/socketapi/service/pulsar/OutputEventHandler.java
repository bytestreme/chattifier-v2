package io.bytestreme.socketapi.service.pulsar;

import io.bytestreme.data.pulsar.AbstractPulsarEvent;

public interface OutputEventHandler<T extends AbstractPulsarEvent> {
    void handle(T abstractPulsarEvent);
}
