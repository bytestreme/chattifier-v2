package io.bytestreme.socketapi.data.ws.input;

import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageSocketEvent extends AbstractSocketEvent {

    private UUID room;
    private String text;

}
