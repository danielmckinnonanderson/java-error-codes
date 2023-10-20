package com.example.javaerrorcodes.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.Optional;

@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record ServerErrorResp(
        boolean success,
        String message,
        Optional<String> errorCode
) {}
