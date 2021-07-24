package io.bytestreme.cloudgateway.filter;

import lombok.extern.slf4j.Slf4j;
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

    private String USER_ID_HEADER = "X-Gateway-ID";

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> exchange.getPrincipal()
                .flatMap(p -> {
                    var req = exchange.getRequest().mutate()
                            .header(USER_ID_HEADER, (String) ((Authentication) p).getPrincipal())
                            .build();
                    return chain.filter(exchange.mutate().request(req).build());
                });
    }

    public static class Config {

    }
}