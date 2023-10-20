package com.example.javaerrorcodes.error;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.http.HttpStatusCode;

import java.util.Optional;

@NoArgsConstructor
public class ThirdPartyApiClientErrorException extends MyApplicationException {

    public static final String ERROR_CODE = "65be8467-b77b-4e39-bd9f-2c25aedb4358";

    private HttpStatusCode statusCode;
    private String apiMessage;

    public ThirdPartyApiClientErrorException(@NonNull HttpStatusCode status, Optional<String> apiMessage) {
        super();

        if (apiMessage.isPresent()) {
            this.apiMessage = apiMessage.get();
        }
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    public HttpStatusCode getStatusCode() {
        return statusCode;
    }

    public Optional<String> getApiMessage() {
        return this.apiMessage == null
                ? Optional.empty()
                : Optional.of(this.apiMessage);
    }
}
