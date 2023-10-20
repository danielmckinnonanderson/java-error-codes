package com.example.javaerrorcodes.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;

import java.util.Optional;

@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record ApiProcessFormErrorResp(
        boolean success,
        Optional<String> message
) { }
