package io.bytestreme.data.pulsar;

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
