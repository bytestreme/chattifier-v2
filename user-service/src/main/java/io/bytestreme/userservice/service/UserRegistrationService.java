package io.bytestreme.userservice.service;

import io.bytestreme.userservice.data.UserRegisterDTO;
import io.bytestreme.userservice.domain.user.UsersByIdRepository;
import io.bytestreme.userservice.domain.user.UsersByIdTable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final UsersByIdRepository byIdRepository;

    public Mono<Void> registerUser(UserRegisterDTO data) {
        return byIdRepository.findByUsername(data.getUsername())
                .flatMap(
                        x -> Mono.<UsersByIdTable>error(
                                () -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "username.taken")
                        )
                )
                .switchIfEmpty(createUser(data))
                .then();
    }

    private Mono<UsersByIdTable> createUser(UserRegisterDTO data) {
        return byIdRepository.save(
                UsersByIdTable
                        .builder()
                        .userId(UUID.randomUUID())
                        .username(data.getUsername().toLowerCase())
                        .pass(passwordEncoder.encode(data.getPassword()))
                        .build()
        );
    }

}
