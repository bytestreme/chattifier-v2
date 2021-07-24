package io.bytestreme.userservice.handler;

import io.bytestreme.userservice.data.UserLoginDTO;
import io.bytestreme.userservice.service.UserLoginService;
import io.bytestreme.userservice.validation.AbstractValidationHandler;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class UserLoginHandler extends AbstractValidationHandler<UserLoginDTO, Validator> {

    private final UserLoginService userLoginService;

    protected UserLoginHandler(Validator validator, UserLoginService userLoginService) {
        super(UserLoginDTO.class, validator);
        this.userLoginService = userLoginService;
    }

    @Override
    protected Mono<ServerResponse> processBody(UserLoginDTO validBody, ServerRequest originalRequest) {
        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(
                        userLoginService.login(validBody),
                        Void.class
                );
    }

}
