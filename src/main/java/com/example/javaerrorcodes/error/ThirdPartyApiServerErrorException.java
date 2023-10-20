package com.example.javaerrorcodes.error;

import org.springframework.http.HttpStatusCode;

/**
 * Exception thrown when our fake vendor API returns a server error
 */
public class ThirdPartyApiServerErrorException extends MyApplicationException {

    public static final String ERROR_CODE = "dd2290fd-b778-43c7-b2a6-a45c40a913f1";

    private HttpStatusCode statusCode;

    public ThirdPartyApiServerErrorException(HttpStatusCode unexpectedStatus) {
        super();
        this.statusCode = unexpectedStatus;
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    public HttpStatusCode getStatusCode() {
        return this.statusCode;
    }
}
