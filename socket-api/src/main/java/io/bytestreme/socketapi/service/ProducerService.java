package io.bytestreme.socketapi.service;

import io.bytestreme.socketapi.data.pulsar.PulsarMessageInputEvent;
import io.bytestreme.socketapi.data.pulsar.mapper.MessageEventMapper;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketEventOutput;
import io.bytestreme.socketapi.data.ws.input.MessageSocketEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final Producer<PulsarMessageInputEvent> messageInputProducer;
    private final MessageEventMapper messageEventMapper;
    private final SocketBytesDecoderService bytesDecoderService;

    public Flux<SocketEventOutput> produceSocketEvent(Flux<SocketEventInput> inputFlux) {
        return inputFlux
                .map(bytesDecoderService::decodeMessageEvent)
                .log()
                .map(event -> {
                    var messageEvent = (MessageSocketEvent) event;
                    return messageEventMapper.map(messageEvent);
                })
                .log()
                .flatMap(x -> Mono.fromFuture(messageInputProducer.sendAsync(x))
                        .map(
                                result -> new SocketEventOutput(
                                        null,
                                        SocketEventOutput.OutputEventType.okIfNotNull(result)
                                )
                        )
                )
                .onErrorReturn(new SocketEventOutput(null, SocketEventOutput.OutputEventType.NOK));
    }

}
