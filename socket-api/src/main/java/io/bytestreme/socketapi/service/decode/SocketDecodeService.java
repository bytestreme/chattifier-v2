package io.bytestreme.socketapi.service.decode;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.Exceptions;
import reactor.core.publisher.Flux;

import java.io.IOException;

@Slf4j
public abstract class SocketDecodeService<T extends AbstractSocketEvent> {

    @Autowired
    private ObjectMapper objectMapper;

    public T decode(SocketEventInput input) {
        return convertFromBytes(input.getData());
    }

    private T convertFromBytes(byte[] bytes) {
        try {
            T decodedObject = objectMapper.readValue(new String(bytes), getType());
            if (getType().isInstance(decodedObject)) {
                return getType().cast(decodedObject);
            } else {
                throw Exceptions.propagate(new SocketException("could not deserialize object from bytes"));
            }
        } catch (Exception e) {
            throw Exceptions.propagate(new SocketException("could not deserialize object from bytes"));
        }
    }

    abstract public int getEventTypeCode();

    abstract Class<T> getType();

}
