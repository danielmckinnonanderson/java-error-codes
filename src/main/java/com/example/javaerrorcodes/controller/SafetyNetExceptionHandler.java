package com.example.javaerrorcodes.controller;

import com.example.javaerrorcodes.error.InvalidFormDataException;
import com.example.javaerrorcodes.error.ThirdPartyApiClientErrorException;
import com.example.javaerrorcodes.error.ThirdPartyApiServerErrorException;
import com.example.javaerrorcodes.response.ServerErrorResp;
import com.fasterxml.jackson.core.JsonParseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@ControllerAdvice
public class SafetyNetExceptionHandler {

    private static final String DEFAULT_MESSAGE = "Something went wrong";
    private final ServerErrorResp defaultResp = new ServerErrorResp(
            false,
            DEFAULT_MESSAGE,
            Optional.empty()
    );

    @ExceptionHandler({
            ThirdPartyApiServerErrorException.class
    })
    public ResponseEntity<ServerErrorResp> apiServerError(ThirdPartyApiServerErrorException e) {
        log.warn("Third party API server returned an error status code. Status was '" + e.getStatusCode().toString() + "'");
        final var body = new ServerErrorResp(false, DEFAULT_MESSAGE, Optional.of(e.getErrorCode()));
        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler({
            ThirdPartyApiClientErrorException.class
    })
    public ResponseEntity<ServerErrorResp> apiClientError(ThirdPartyApiClientErrorException e) {
        log.warn("Third party API server returned a client error status code. Status was '" + e.getStatusCode().toString() + "'");
        final var body = new ServerErrorResp(false, DEFAULT_MESSAGE, Optional.of(e.getErrorCode()));
        return ResponseEntity.internalServerError().body(body);
    }

    @ExceptionHandler({
            HttpRequestMethodNotSupportedException.class
    })
    public ResponseEntity<Void> methodNotSupported(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).build();
    }

    @ExceptionHandler({
            InvalidFormDataException.class,
            MethodArgumentNotValidException.class,
            HttpMessageNotReadableException.class,
            JsonParseException.class
    })
    public ResponseEntity<ServerErrorResp> badRequest(Exception e) {
        if (e instanceof InvalidFormDataException) {
            final var exception = (InvalidFormDataException) e;
            final var errMessage = exception.getInvalidFieldsMessages()
                    .entrySet()
                    .stream()
                    .map((entry) ->
                            String.format("'%s' %s", entry.getKey(), entry.getValue()))
                    .collect(Collectors.joining(" "));

            final var body =  new ServerErrorResp(false, errMessage, Optional.of(((InvalidFormDataException) e).getErrorCode()));
            return ResponseEntity.badRequest().body(body);
        }

        if (e instanceof HttpMessageNotReadableException) {
            return ResponseEntity.badRequest()
                    .body(ServerErrorResp.builder()
                            .message(e.getMessage())
                            .build());
        }

        return ResponseEntity.badRequest().body(defaultResp);
    }

    @ExceptionHandler({
            Exception.class
    })
    public ResponseEntity<ServerErrorResp> uncaughtException(Exception e) {
        log.error("Safety net caught an exception");
        e.printStackTrace();
        return ResponseEntity.internalServerError().body(defaultResp);
    }
}
