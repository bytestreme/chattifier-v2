package io.bytestreme.socketapi.data.pulsar;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class PulsarMessageEditEvent extends AbstractPulsarEvent {

    private UUID sender;
    private UUID room;
    private UUID messageId;
    private String content;
    private Long timestamp;

    @Override
    public int getEventType() {
        return SocketEventInput.InputEventType.MESSAGE_EDIT;
    }
}
