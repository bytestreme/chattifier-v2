package io.bytestreme.data.pulsar.event.input;

import io.bytestreme.data.pulsar.PulsarTypeCodes;

import java.util.UUID;

public class PulsarMessagePersistRequestInputEvent extends AbstractPulsarEvent {

    private UUID sender;
    private UUID room;
    private String content;
    private Long timestamp;

    public PulsarMessagePersistRequestInputEvent(UUID sender, UUID room, String content, Long timestamp) {
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = timestamp;
    }

    public PulsarMessagePersistRequestInputEvent() {
    }

    @Override
    public int getEventType() {
        return PulsarTypeCodes.InputEventType.PERSIST_REQUEST_IN;
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
