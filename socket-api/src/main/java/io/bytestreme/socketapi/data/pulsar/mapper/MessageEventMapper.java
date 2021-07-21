package io.bytestreme.socketapi.data.pulsar.mapper;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageInputEvent;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.input.MessageSocketEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MessageEventMapper implements SocketToPulsarEventMapper<MessageSocketEvent, PulsarMessageInputEvent>{

    @Override
    public PulsarMessageInputEvent map(AbstractSocketEvent socketEvent) {
        var result = new PulsarMessageInputEvent();
        var message = (MessageSocketEvent) socketEvent;
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
