package io.bytestreme.socketapi.data.ws.input;

import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
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
public class MessageEditSocketEvent extends AbstractSocketEvent {

    private UUID message;
    private UUID room;
    private String text;

    @Override
    public int getType() {
        return SocketEventInput.InputEventType.MESSAGE_EDIT;
    }
}
