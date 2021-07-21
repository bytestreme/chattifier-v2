package io.bytestreme.socketapi.config;

import io.bytestreme.socketapi.data.pulsar.AbstractPulsarEvent;
import io.bytestreme.socketapi.data.pulsar.mapper.SocketToPulsarEventMapper;
import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.data.ws.SocketEventInput;
import io.bytestreme.socketapi.service.decode.SocketDecodeService;
import io.bytestreme.socketapi.util.ProcessingWrapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.Producer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Configuration
public class ProcessingWrapperConfig {

    private static final Integer[] DECLARED_SERVICES = new Integer[]{
            SocketEventInput.InputEventType.MESSAGE_IN, SocketEventInput.InputEventType.MESSAGE_EDIT
    };

    @Bean(name = "processingWrapperConfigMap")
    public Map<Integer, ProcessingWrapper>
    processingWrapperConfigMap(List<Producer> producers,
                               List<SocketDecodeService<? extends AbstractSocketEvent>> decoders,
                               List<SocketToPulsarEventMapper<? extends AbstractSocketEvent, ? extends AbstractPulsarEvent>> mappers) {
        var result = new HashMap<Integer, ProcessingWrapper>(DECLARED_SERVICES.length);
        var producersMap = producers.stream()
                .collect(
                        Collectors
                                .toMap(
                                        p -> Integer.valueOf(p.getProducerName()),
                                        s -> s
                                )
                );
        log.warn("producersMap: " + producersMap.size());

        var decodersMap = decoders.stream()
                .collect(
                        Collectors
                                .toMap(
                                        SocketDecodeService::getEventTypeCode,
                                        s -> s
                                )
                );
        log.warn("decodersMap: " + decodersMap.size());

        var mappersMap = mappers.stream()
                .collect(
                        Collectors
                                .toMap(
                                        SocketToPulsarEventMapper::getTypeCode,
                                        s -> s
                                )
                );
        log.warn("mappers: " + mappers.size());
        for (int key : DECLARED_SERVICES) {
            ProcessingWrapper wrapper = new ProcessingWrapper();
            wrapper.setPulsarProducer(producersMap.get(key));
            wrapper.setSocketDecodeService(decodersMap.get(key));
            wrapper.setSocketToPulsarEventMapper(mappersMap.get(key));
            wrapper.setTypeCode(key);
            result.put(key, wrapper);
        }
        return result;
    }

}
