package io.bytestreme.socketapi.data.pulsar.mapper;

import io.bytestreme.data.pulsar.event.PulsarMessageInputEvent;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageInSocketEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MessageInEventMapper implements SocketToPulsarEventMapper<MessageInSocketEvent, PulsarMessageInputEvent> {

    @Override
    public PulsarMessageInputEvent map(AbstractSocketEvent socketEvent) {
        var result = new PulsarMessageInputEvent();
        var message = (MessageInSocketEvent) socketEvent;
        result.setContent(message.getText());
        result.setSender(getCurrentUser());
        result.setTimestamp(Instant.now().getEpochSecond());
        result.setRoom(message.getRoom());
        return result;
    }

    @Override
    public int getTypeCode() {
        return SocketEventInput.InputEventType.MESSAGE_IN;
    }

    private UUID getCurrentUser() {
        return UUID.randomUUID();
    }

}
