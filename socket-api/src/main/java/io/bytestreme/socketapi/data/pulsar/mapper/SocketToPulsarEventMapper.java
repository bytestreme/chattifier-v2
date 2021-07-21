package io.bytestreme.socketapi.data.pulsar.mapper;

import io.bytestreme.socketapi.data.pulsar.AbstractPulsarEvent;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;

public interface SocketToPulsarEventMapper<SOCKET_EVENT extends AbstractSocketEvent, PULSAR_EVENT extends AbstractPulsarEvent> {
    PULSAR_EVENT map(AbstractSocketEvent socketEvent);
    int getTypeCode();
}
