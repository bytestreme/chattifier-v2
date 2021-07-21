package io.bytestreme.socketapi.data.pulsar.mapper;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageEditEvent;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageEditSocketEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MessageEditEventMapper implements SocketToPulsarEventMapper<MessageEditSocketEvent, PulsarMessageEditEvent> {

    @Override
    public PulsarMessageEditEvent map(AbstractSocketEvent socketEvent) {
        var result = new PulsarMessageEditEvent();
        var message = (MessageEditSocketEvent) socketEvent;
        result.setMessageId(message.getMessage());
        result.setContent(message.getText());
        result.setSender(getCurrentUser());
        result.setTimestamp(Instant.now().getEpochSecond());
        result.setRoom(message.getRoom());
        return result;
    }

    @Override
    public int getTypeCode() {
        return SocketEventInput.InputEventType.MESSAGE_EDIT;
    }

    private UUID getCurrentUser() {
        return UUID.randomUUID();
    }
}
