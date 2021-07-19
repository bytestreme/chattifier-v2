package io.bytestreme.socketapi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessageOutput {

    private byte[] data;
    private int type;

    public static class OutputEventType {
        public static final int MESSAGE = 1;
        public static final int OK = 2;
        public static final int NOK = 4;

        public static int ackIfNull(Object obj) {
            return obj == null ? NOK : OK;
        }
    }

}
