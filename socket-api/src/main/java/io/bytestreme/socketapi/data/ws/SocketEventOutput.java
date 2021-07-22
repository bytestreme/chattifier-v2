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

}
