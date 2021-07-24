package io.bytestreme.userservice.service;

import io.bytestreme.userservice.data.UserLoginDTO;
import io.bytestreme.userservice.domain.user.UsersByIdRepository;
import io.bytestreme.userservice.security.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserLoginService {

    private static final String TOKEN_VERSION_KEY = "token-version";

    private final UsersByIdRepository byIdRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final ReactiveRedisTemplate<String, String> redisTemplate;

    @NonNull
    public Mono<ResponseEntity<Map<String, String>>> login(UserLoginDTO loginDTO) {
        String version = UUID.randomUUID().toString();
        return byIdRepository.findByUsername(loginDTO.getUsername())
                .filter(u -> passwordEncoder.matches(loginDTO.getPassword(), u.getPass()))
                .map(z -> tokenService.generateToken(z, version))
                .doOnNext(user -> byIdRepository.findByUsername(loginDTO.getUsername())
                        .flatMap(us -> redisTemplate.opsForHash().put(TOKEN_VERSION_KEY, us.getUserId().toString(), version))
                        .subscribe()
                )
                .map(token -> ResponseEntity.ok()
                        .body(
                                Map.of("login.success", token)
                        )
                )
                .switchIfEmpty(
                        Mono.just(
                                ResponseEntity.badRequest()
                                        .body(
                                                Map.of("login.failed", "")
                                        )
                        )
                );
    }
}
