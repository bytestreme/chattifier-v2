package io.bytestreme.cloudgateway.security;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final TokenService tokenService;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @Value("${jwt.claim.token-version}")
    private String JWT_VERSION_CLAIM;

    @Value("${redis.key.token-version}")
    private String REDIS_TOKEN_VERSION_KEY;

    @Override
    @SuppressWarnings("unchecked")
    public Mono<Authentication> authenticate(Authentication authentication) {
        String authToken = authentication.getCredentials().toString();
        String username;
        try {
            username = tokenService.getUsernameFromToken(authToken);
        } catch (Exception e) {
            username = null;
        }
        if (username != null) {
            Claims claims = tokenService.getAllClaimsFromToken(authToken);
            String tokenVersion = claims.get(JWT_VERSION_CLAIM, String.class);

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, username, List.of());
            SecurityContextHolder.getContext().setAuthentication(new AuthenticatedUser(username, List.of()));

            return redisTemplate
                    .opsForHash()
                    .<String>get(REDIS_TOKEN_VERSION_KEY, username)
                    .filter(x -> x.equals(tokenVersion))
                    .flatMap(y -> Mono.<Authentication>just(auth))
                    .switchIfEmpty(
                            Mono.error(new BadCredentialsException("token.revoked"))
                    );
        } else {
            return Mono.error(new BadCredentialsException("token.invalid"));
        }
    }
}