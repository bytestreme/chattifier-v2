package io.bytestreme.socketapi.config;

import io.bytestreme.socketapi.data.ws.AbstractSocketEvent;
import io.bytestreme.socketapi.service.decode.SocketDecodeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Configuration
public class SocketDecoderConfig {

    @Bean(name = "socketDecoderMap")
    public Map<Integer, SocketDecodeService<? extends AbstractSocketEvent>>
    socketDecoderMap(List<SocketDecodeService<? extends AbstractSocketEvent>> services) {
        log.warn("services " + services.stream().map(SocketDecodeService::getEventTypeCode).collect(Collectors.toList()));
        return services.stream()
                .collect(
                        Collectors
                                .toMap(
                                        SocketDecodeService::getEventTypeCode,
                                        s -> s
                                )
                );
    }
}
