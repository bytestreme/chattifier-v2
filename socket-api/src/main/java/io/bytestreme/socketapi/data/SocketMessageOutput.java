package io.bytestreme.socketapi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SocketMessageOutput {
    private String data;

    public static SocketMessageOutput pong() {
        return new SocketMessageOutput("pong");
    }
}
