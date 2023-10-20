package com.example.javaerrorcodes.error;

public abstract class MyApplicationException extends RuntimeException {
    public static String ERROR_CODE;

    public abstract String getErrorCode();
}
