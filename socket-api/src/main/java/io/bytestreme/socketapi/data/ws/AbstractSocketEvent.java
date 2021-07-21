package io.bytestreme.socketapi.data.ws;

import lombok.Data;

@Data
abstract public class AbstractSocketEvent {
    public abstract int getType();
}
