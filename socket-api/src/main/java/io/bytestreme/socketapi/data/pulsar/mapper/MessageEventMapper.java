package io.bytestreme.socketapi.data.pulsar.mapper;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageInputEvent;
import io.bytestreme.socketapi.data.ws.input.MessageSocketEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class MessageEventMapper implements SocketToPulsarEventMapper<MessageSocketEvent, PulsarMessageInputEvent>{

    @Override
    public PulsarMessageInputEvent map(MessageSocketEvent socketEvent) {
        var result = new PulsarMessageInputEvent();
        result.setContent(socketEvent.getText());
        result.setSender(getCurrentUser());
        result.setTimestamp(Instant.now().getEpochSecond());
        result.setRoom(socketEvent.getRoom());
        return result;
    }

    private UUID getCurrentUser() {
        return UUID.randomUUID();
    }

}
