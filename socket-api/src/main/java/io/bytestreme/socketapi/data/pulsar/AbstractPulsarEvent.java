package io.bytestreme.socketapi.data.pulsar;

import lombok.Data;

@Data
public abstract class AbstractPulsarEvent {

    public abstract int getEventType();
}
