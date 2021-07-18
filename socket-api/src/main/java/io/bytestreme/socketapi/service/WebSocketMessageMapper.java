package io.bytestreme.socketapi.service;

import io.bytestreme.socketapi.data.SocketMessageInput;
import io.bytestreme.socketapi.data.SocketMessageOutput;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class WebSocketMessageMapper {

    private final Jackson2JsonEncoder encoder;
    private final Jackson2JsonDecoder decoder;

    public Flux<SocketMessageInput> decode(Flux<DataBuffer> inbound) {
        return inbound.flatMap(p -> decoder.decode(
                Mono.just(p),
                ResolvableType.forType(
                        new ParameterizedTypeReference<SocketMessageInput>() {
                        }),
                MediaType.APPLICATION_JSON,
                Collections.emptyMap()
        ))
                .map(o -> (SocketMessageInput) o);
    }

    public Flux<DataBuffer> encode(Flux<SocketMessageOutput> outbound, DataBufferFactory dataBufferFactory) {
        return outbound
                .flatMap(i -> encoder
                        .encode(
                                Mono.just(i),
                                dataBufferFactory,
                                ResolvableType.forType(Object.class),
                                MediaType.APPLICATION_JSON,
                                Collections.emptyMap()
                        )
                );
    }

}
