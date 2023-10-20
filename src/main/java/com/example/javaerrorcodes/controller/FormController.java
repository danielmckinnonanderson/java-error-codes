package com.example.javaerrorcodes.controller;

import com.example.javaerrorcodes.error.InvalidFormDataException;
import com.example.javaerrorcodes.error.ThirdPartyApiServerErrorException;
import com.example.javaerrorcodes.form.ProcessFormReq;
import com.example.javaerrorcodes.service.ProcessFormService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController(value = "/form")
public class FormController {

    @Autowired
    ProcessFormService formService;

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/process",
            produces = "application/json",
            consumes = "application/json"
    )
    public ResponseEntity<Void> processForm(@Valid @RequestBody ProcessFormReq req, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            final Map<String, String> errMessageMap = bindingResult.getAllErrors()
                    .stream()
                    .collect(
                        Collectors.toMap(ObjectError::getObjectName, ObjectError::getDefaultMessage,
                        (existing, replacement) -> existing));

            throw new InvalidFormDataException(errMessageMap);
        }


        final var result = this.formService.process(req);

        // If we even reached this point without throwing an exception, we're gravy.
        return ResponseEntity.accepted().build();
    }

    @RequestMapping(
            method = RequestMethod.POST,
            value = "/test"
    )
    public ResponseEntity<Void> testError() throws ThirdPartyApiServerErrorException {
        throw new ThirdPartyApiServerErrorException(HttpStatus.GATEWAY_TIMEOUT);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            value = "/"
    )
    public ResponseEntity<Void> testSuccess() {
        return ResponseEntity.ok().build();
    }
}
