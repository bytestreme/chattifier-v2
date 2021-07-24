package io.bytestreme.cloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthFilter extends AbstractGatewayFilterFactory<AuthFilter.Config> {

    public AuthFilter() {
        super(Config.class);
    }

    @Value("${token.forwarded.user-id}")
    private String USER_ID_HEADER;

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .flatMap(p ->
                        chain
                                .filter(
                                        exchange.mutate()
                                                .request(
                                                        exchange.getRequest().mutate()
                                                                .header(
                                                                        USER_ID_HEADER,
                                                                        (String) ((Authentication) p).getPrincipal()
                                                                )
                                                                .build()
                                                )
                                                .build()
                                )
                );
    }

    public static class Config {

    }
}