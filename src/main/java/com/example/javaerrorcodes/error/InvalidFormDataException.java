package com.example.javaerrorcodes.error;

import java.util.Map;

public class InvalidFormDataException extends MyApplicationException {

    public static final String ERROR_CODE = "43666bb9-1e1a-4e3a-87bb-f1c6caab1e64";

    // Map of object field name to its error message
    private Map<String, String> invalidFieldsMessages;

    public InvalidFormDataException(Map<String, String> invalidFieldsMessages) {
        this.invalidFieldsMessages = invalidFieldsMessages;
    }

    @Override
    public String getErrorCode() {
        return ERROR_CODE;
    }

    public Map<String, String> getInvalidFieldsMessages() {
        return this.invalidFieldsMessages;
    }
}
