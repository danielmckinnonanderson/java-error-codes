package com.example.javaerrorcodes.form;

import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public record ApiProcessFormSuccessResp(
        boolean success,
        ProcessFormReq data
) { }
