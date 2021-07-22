package io.bytestreme.socketapi.service;

import io.bytestreme.data.pulsar.PulsarTypeCodes;
import io.bytestreme.socketapi.data.pulsar.mapper.MessageInEventMapper;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.data.ws.SocketEventOutput;
import io.bytestreme.socketapi.util.ProcessingWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProducerService {

    private final Map<Integer, ProcessingWrapper> processingWrapperConfigMap;
    private final SocketBytesDecoderService bytesDecoderService;

    public Flux<SocketEventOutput> produceSocketEvent(Flux<SocketEventInput> inputFlux) {
        return inputFlux
                .map(bytesDecoderService::decodeMessageEvent)
                .log()
                .map(event -> processingWrapperConfigMap
                        .get(event.getType())
                        .getSocketToPulsarEventMapper()
                        .map(event)
                )
                .log()
                .flatMap(x -> Mono.fromFuture(
                        processingWrapperConfigMap
                                .get(x.getEventType())
                                .getPulsarProducer()
                                .sendAsync(x)
                        )
                                .map(
                                        result -> new SocketEventOutput(
                                                null,
                                                result == null
                                                        ? PulsarTypeCodes.OutputEventType.NOK
                                                        : PulsarTypeCodes.OutputEventType.OK
                                        )
                                )
                )
                .onErrorReturn(new SocketEventOutput(null, PulsarTypeCodes.OutputEventType.NOK));
    }

}
