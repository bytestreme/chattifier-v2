package io.bytestreme.data.pulsar.event.output;

import io.bytestreme.data.pulsar.event.input.AbstractPulsarEvent;

import java.util.UUID;

public abstract class AbstractPulsarOutputEvent extends AbstractPulsarEvent {

    private final UUID target;

    protected AbstractPulsarOutputEvent(UUID target) {
        this.target = target;
    }

    public UUID getTarget() {
        return target;
    }

}
