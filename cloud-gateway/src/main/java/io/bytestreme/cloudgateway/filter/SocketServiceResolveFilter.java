package io.bytestreme.cloudgateway.filter;

import io.bytestreme.cloudgateway.service.UserResolvingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

//@Slf4j
//@Component
public class SocketServiceResolveFilter extends AbstractGatewayFilterFactory<SocketServiceResolveFilter.Config> {

    @Override
    public Class<Config> getConfigClass() {
        return SocketServiceResolveFilter.Config.class;
    }

    @Value("${nameRegistry.socketApi}")
    private String socketApiServiceName;

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private UserResolvingService userResolvingService;

    @Autowired
    private ReactiveRedisTemplate<String, String> redisTemplate;


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> userResolvingService
                .getCurrentAuthentication()
                .map(Principal::getName)
                .flatMap(uid -> redisTemplate.opsForHash().get(socketApiServiceName, uid))
                .switchIfEmpty(Mono.just(getServiceUriString(socketApiServiceName)))
                .doOnNext(routeUri -> userResolvingService
                        .getCurrentAuthentication()
                        .map(Principal::getName)
                        .flatMap(uidInner -> redisTemplate.opsForHash().put(socketApiServiceName, uidInner, routeUri))
                )
                .flatMap(url -> {
                    String urlString = (String) url;
                    try {
                        exchange.getAttributes().put(ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR, new URI(urlString));
                    } catch (URISyntaxException e) {
//                        log.error(e.getMessage());
                    }
                    return chain.filter(exchange);
                });
    }

    private String getServiceUriString(String socketApiServiceName) {
        var availableServices = this.discoveryClient.getInstances(socketApiServiceName);
        if (availableServices.isEmpty()) {
            throw new IllegalStateException("no services available");
        } else {
            return availableServices.iterator().next().getUri().getRawPath();
        }
    }


    public static class Config {

    }
}
