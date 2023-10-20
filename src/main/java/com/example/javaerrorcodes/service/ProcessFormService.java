package com.example.javaerrorcodes.service;

import com.example.javaerrorcodes.error.InvalidFormDataException;
import com.example.javaerrorcodes.error.ThirdPartyApiClientErrorException;
import com.example.javaerrorcodes.error.ThirdPartyApiServerErrorException;
import com.example.javaerrorcodes.form.ApiProcessFormErrorResp;
import com.example.javaerrorcodes.form.ApiProcessFormSuccessResp;
import com.example.javaerrorcodes.form.ProcessFormReq;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;

@Service
@Slf4j
public class ProcessFormService {

    @Autowired FakeThirdPartyApiService apiService;

    private static final Set<String> VALID_COLORS = Set.of(
            "blue",
            "red",
            "yellow",
            "green",
            "purple",
            "orange",
            "black",
            "white",
            "grey",
            "gray"
    );


    /**
     *  Process a form by validating its contents and then POST'ing it to a fake API.
     *
     *  The fake API is notoriously flaky and is sure to fail on us, so we better handle
     *    all possible response status codes by throwing custom exceptions,
     *    so that we can send a descriptive error code back to our client.
     */
    public ApiProcessFormSuccessResp process(ProcessFormReq formData) {
        final var valid = this.validateForm(formData);

        if (!valid)
            // Can't be bothered to do more with this
            throw new InvalidFormDataException(new HashMap<>());

        final var response = this.apiService.postForm(formData);

        if (response.getStatusCode().is5xxServerError()) {
            throw new ThirdPartyApiServerErrorException(response.getStatusCode());
        }

        if (response.getStatusCode().is4xxClientError()) {
            final var body = (ApiProcessFormErrorResp) response.getBody();

            throw new ThirdPartyApiClientErrorException(response.getStatusCode(), body.message());
        }

        if (response.getStatusCode().is2xxSuccessful()) {
            final var body = (ApiProcessFormSuccessResp) response.getBody();

            return body;
        }

        // Fallthrough
        throw new RuntimeException("Something fishy happened.");
    }

    public boolean validateForm(ProcessFormReq formData) {
        if (!VALID_COLORS.contains(formData.getFavoriteColor().toLowerCase(Locale.US))) {
            return false;
        }

        return true;
    }
}
