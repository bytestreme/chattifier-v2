package io.bytestreme.socketapi.data.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketEventInput {

    private byte[] data;
    private int type;

    public static class InputEventType {
        public static final int MESSAGE_IN = -1;
        public static final int MESSAGE_EDIT = -1;
    }
}

