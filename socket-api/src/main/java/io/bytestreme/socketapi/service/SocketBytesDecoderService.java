package io.bytestreme.socketapi.service;

import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.service.decode.SocketDecodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;

import java.util.Map;

@Service
@Slf4j
public class SocketBytesDecoderService {

    @Autowired
    @Qualifier("socketDecoderMap")
    private Map<Integer, SocketDecodeService<?>> socketDecoderMap;

    public AbstractSocketEvent decodeMessageEvent(SocketEventInput messageInput) {
        var decoder = socketDecoderMap.get(messageInput.getType());
        if (decoder != null) {
            return decoder.decode(messageInput);
        } else {
            throw Exceptions.propagate(new IllegalArgumentException("No decoder for given event type"));
        }
    }

}
