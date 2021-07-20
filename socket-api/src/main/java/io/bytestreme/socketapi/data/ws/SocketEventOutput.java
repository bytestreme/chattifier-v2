package io.bytestreme.socketapi.data.ws;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketEventOutput {

    private byte[] data;
    private int type;

    public static class OutputEventType {
        public static final int MESSAGE_OUT = 1;
        public static final int OK = 2;
        public static final int NOK = 4;

        public static int okIfNotNull(Object obj) {
            return obj == null ? NOK : OK;
        }
    }

}
