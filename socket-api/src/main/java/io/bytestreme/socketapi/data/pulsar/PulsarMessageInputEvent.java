package io.bytestreme.socketapi.data.pulsar;

import io.bytestreme.socketapi.data.ws.SocketEventInput;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PulsarMessageInputEvent extends AbstractPulsarEvent {
    private UUID sender;
    private UUID room;
    private String content;
    private Long timestamp;

    @Override
    public int getEventType() {
        return SocketEventInput.InputEventType.MESSAGE_IN;
    }

}
