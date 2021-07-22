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

}

