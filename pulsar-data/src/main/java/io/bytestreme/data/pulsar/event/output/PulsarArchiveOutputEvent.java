package io.bytestreme.data.pulsar.event.output;

import io.bytestreme.data.pulsar.PulsarTypeCodes;

import java.util.UUID;

public class PulsarArchiveOutputEvent extends AbstractPulsarOutputEvent {

    private UUID sender;
    private UUID room;
    private String content;
    private Long timestamp;

    public PulsarArchiveOutputEvent() {
        super(null);
    }

    public PulsarArchiveOutputEvent(UUID target) {
        super(target);
    }

    public PulsarArchiveOutputEvent(UUID target, UUID sender, UUID room, String content, Long timestamp) {
        super(target);
        this.sender = sender;
        this.room = room;
        this.content = content;
        this.timestamp = timestamp;
    }

    @Override
    public int getEventType() {
        return PulsarTypeCodes.OutputEventType.ARCHIVE_OUT;
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
