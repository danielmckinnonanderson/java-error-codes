package com.example.javaerrorcodes.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;

/**
 * Object representing the body of a POST request to /form/submit
 */
@Builder
@Getter
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ProcessFormReq {
    @NotNull
    private final String favoriteColor;

    @NotNull
    private final Integer pickANumber;
}
