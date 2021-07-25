package io.bytestreme.socketapi.util;

import io.bytestreme.data.pulsar.event.input.AbstractPulsarEvent;
import io.bytestreme.socketapi.data.pulsar.mapper.SocketToPulsarEventMapper;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.service.decode.SocketDecodeService;
import lombok.Data;
import org.apache.pulsar.client.api.Producer;

@Data
public class ProcessingWrapper {
    private Producer<AbstractPulsarEvent> pulsarProducer;
    private SocketToPulsarEventMapper<? extends AbstractSocketEvent, ? extends AbstractPulsarEvent> socketToPulsarEventMapper;
    private SocketDecodeService<? extends AbstractSocketEvent> socketDecodeService;
    private int typeCode;
}
