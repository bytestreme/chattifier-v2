package io.bytestreme.socketapi.config;

import io.bytestreme.socketapi.handler.SocketEventHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.HandlerAdapter;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.HandshakeWebSocketService;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import org.springframework.web.reactive.socket.server.upgrade.ReactorNettyRequestUpgradeStrategy;

import java.util.Map;

@Configuration
public class WebSocketConfig {

    @Bean
    public HandlerMapping webSocketHandler(SocketEventHandler socketEventHandler) {
        return new SimpleUrlHandlerMapping() {{
            setUrlMap(Map.of(
                    "api/test", socketEventHandler
            ));
            setOrder(0);
        }};
    }

    @Bean
    public HandlerAdapter webSocketHandlerAdapter() {
        return new WebSocketHandlerAdapter(
                new HandshakeWebSocketService(
                        new ReactorNettyRequestUpgradeStrategy()
                )
        );
    }

}
