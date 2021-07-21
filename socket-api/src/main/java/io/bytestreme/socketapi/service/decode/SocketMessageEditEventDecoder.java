package io.bytestreme.socketapi.service.decode;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageEditSocketEvent;
import org.springframework.stereotype.Service;

@Service
public class SocketMessageEditEventDecoder extends SocketDecodeService<MessageEditSocketEvent> {

    @Override
    public int getEventTypeCode() {
        return SocketEventInput.InputEventType.MESSAGE_EDIT;
    }

    @Override
    Class<MessageEditSocketEvent> getType() {
        return MessageEditSocketEvent.class;
    }
}
