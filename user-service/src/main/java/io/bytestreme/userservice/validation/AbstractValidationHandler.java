package io.bytestreme.userservice.validation;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;


public abstract class AbstractValidationHandler<T, U extends Validator> {

    private final Class<T> validationClass;

    private final U validator;

    protected AbstractValidationHandler(Class<T> clazz, U validator) {
        this.validationClass = clazz;
        this.validator = validator;
    }

    public Mono<ServerResponse> handleRequest(final ServerRequest request) {
        return request.bodyToMono(this.validationClass)
                .flatMap(body -> {
                    Errors errors = new BeanPropertyBindingResult(
                            body,
                            this.validationClass.getName());
                    this.validator.validate(body, errors);

                    if (errors.getAllErrors().isEmpty()) {
                        return processBody(body, request);
                    } else {
                        return onValidationErrors(errors, body, request);
                    }
                });
    }

    protected Mono<ServerResponse> onValidationErrors(
            Errors errors,
            T invalidBody,
            ServerRequest request) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                errors.getAllErrors()
                        .stream()
                        .map(error -> error == null ? "" : error.getDefaultMessage())
                        .collect(Collectors.joining(", "))
        );
    }

    abstract protected Mono<ServerResponse> processBody(
            T validBody,
            ServerRequest originalRequest
    );
}