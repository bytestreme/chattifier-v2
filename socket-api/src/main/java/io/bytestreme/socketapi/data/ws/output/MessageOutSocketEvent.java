package io.bytestreme.socketapi.data.ws.output;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class MessageOutSocketEvent extends AbstractSocketEvent {
    private UUID sender;
    private UUID room;
    private String content;
    private Long timestamp;

    @Override
    public int getType() {
        return PulsarTypeCodes.OutputEventType.MESSAGE_OUT;
    }
}
