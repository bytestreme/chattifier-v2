package io.bytestreme.socketapi.service.decode;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageInSocketEvent;
import org.springframework.stereotype.Service;

@Service
public class SocketMessageInEventDecoder extends SocketDecodeService<MessageInSocketEvent> {

    @Override
    public int getEventTypeCode() {
        return SocketEventInput.InputEventType.MESSAGE_IN;
    }

    @Override
    Class<MessageInSocketEvent> getType() {
        return MessageInSocketEvent.class;
    }

}
