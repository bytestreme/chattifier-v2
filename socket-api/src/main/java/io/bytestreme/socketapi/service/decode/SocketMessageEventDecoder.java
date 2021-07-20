package io.bytestreme.socketapi.service.decode;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageSocketEvent;
import org.springframework.stereotype.Service;

@Service
public class SocketMessageEventDecoder extends SocketDecodeService<MessageSocketEvent> {

    @Override
    public int getEventTypeCode() {
        return SocketEventInput.InputEventType.MESSAGE_IN;
    }

    @Override
    Class<MessageSocketEvent> getType() {
        return MessageSocketEvent.class;
    }

}
