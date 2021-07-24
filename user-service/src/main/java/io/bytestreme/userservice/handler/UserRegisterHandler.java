package io.bytestreme.userservice.handler;

import io.bytestreme.userservice.data.UserRegisterDTO;
import io.bytestreme.userservice.service.UserRegistrationService;
import io.bytestreme.userservice.validation.AbstractValidationHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserRegisterHandler extends AbstractValidationHandler<UserRegisterDTO, Validator> {

    private final UserRegistrationService userRegistrationService;

    public UserRegisterHandler(Validator validator,
                               UserRegistrationService userRegistrationService) {
        super(UserRegisterDTO.class, validator);
        this.userRegistrationService = userRegistrationService;
    }

    @Override
    protected Mono<ServerResponse> processBody(UserRegisterDTO validBody, ServerRequest originalRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userRegistrationService.registerUser(validBody),
                        Void.class
                );
    }
}
