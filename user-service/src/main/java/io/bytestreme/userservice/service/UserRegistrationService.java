package io.bytestreme.userservice.service;

import io.bytestreme.userservice.data.UserRegisterDTO;
import io.bytestreme.userservice.domain.id.UserById;
import io.bytestreme.userservice.domain.id.UserByIdRepository;
import io.bytestreme.userservice.domain.username.UserByUsername;
import io.bytestreme.userservice.domain.username.UserByUsernameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserRegistrationService {

    private final PasswordEncoder passwordEncoder;
    private final UserByIdRepository byIdRepository;
    private final UserByUsernameRepository byUsernameRepository;

    public Mono<UserById> registerUser(UserRegisterDTO data) {
        return findUserByUsername(data.getUsername())
                .switchIfEmpty(createUser(data))
                .flatMap(x -> Mono.error(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "username.taken")));
//        return findUserByUsername(data.getUsername())
//                .flatMap(x -> Mono.error(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "username.taken")))
//                .switchIfEmpty(createUser(data));
    }

    private Mono<UserById> createUser(UserRegisterDTO data) {
        var userId = UUID.randomUUID();

        var byId = new UserById(
                userId,
                data.getUsername(),
                passwordEncoder.encode(data.getUsername()),
                Collections.emptyList()
        );
        var byUsername = new UserByUsername(
                data.getUsername(),
                Instant.now(),
                userId
        );
        return byUsernameRepository.save(byUsername)
                .flatMap(x -> byIdRepository.save(byId));
    }

    public Mono<UserById> findUserByUsername(String username) {
        return byUsernameRepository.findUserByUsernameUsername(username)
                .flatMap(user -> byIdRepository.findByUserId(user.getUserId()));
    }


}
