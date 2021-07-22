package io.bytestreme.data.pulsar.event;

import io.bytestreme.data.pulsar.AbstractPulsarEvent;
import io.bytestreme.data.pulsar.PulsarTypeCodes;


import java.util.UUID;


public class PulsarMessageInputEvent extends AbstractPulsarEvent {
    private UUID sender;
    private UUID room;
    private String content;
    private Long timestamp;

    public PulsarMessageInputEvent(UUID sender, UUID room, String content, Long timestamp) {
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = timestamp;
    }

    public PulsarMessageInputEvent() {
    }

    @Override
    public int getEventType() {
        return PulsarTypeCodes.InputEventType.MESSAGE_IN;
    }

    public UUID getSender() {
        return sender;
    }

    public void setSender(UUID sender) {
        this.sender = sender;
    }

    public UUID getRoom() {
        return room;
    }

    public void setRoom(UUID room) {
        this.room = room;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
