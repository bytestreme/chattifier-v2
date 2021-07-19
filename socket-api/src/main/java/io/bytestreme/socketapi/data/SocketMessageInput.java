package io.bytestreme.socketapi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessageInput {

    private byte[] data;
    private int type;

    public static class InputEventType {
        public static final int MESSAGE = -1;

    }
}

