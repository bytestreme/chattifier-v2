package io.bytestreme.data.pulsar.event;

import io.bytestreme.data.pulsar.AbstractPulsarEvent;
import io.bytestreme.data.pulsar.PulsarTypeCodes;


import java.util.UUID;


public class PulsarMessageEditEvent extends AbstractPulsarEvent {

    private UUID sender;
    private UUID room;
    private UUID messageId;
    private String content;
    private Long timestamp;

    public PulsarMessageEditEvent(UUID sender, UUID room, UUID messageId, String content, Long timestamp) {
        this.sender = sender;
        this.room = room;
        this.messageId = messageId;
        this.content = content;
        this.timestamp = timestamp;
    }

    public PulsarMessageEditEvent() {
    }

    @Override
    public int getEventType() {
        return PulsarTypeCodes.InputEventType.MESSAGE_EDIT;
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

    public UUID getMessageId() {
        return messageId;
    }

    public void setMessageId(UUID messageId) {
        this.messageId = messageId;
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
