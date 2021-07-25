package io.bytestreme.data.pulsar.event.input;

import io.bytestreme.data.pulsar.PulsarTypeCodes;

import java.util.UUID;

public class PulsarArchiveInputEvent extends AbstractPulsarEvent {

    private UUID user;

    public PulsarArchiveInputEvent(UUID user) {
        this.user = user;
    }

    public PulsarArchiveInputEvent() {
    }

    @Override
    public int getEventType() {
        return PulsarTypeCodes.InputEventType.ARCHIVE_IN;
    }

    public UUID getUser() {
        return user;
    }

    public void setUser(UUID user) {
        this.user = user;
    }
}
