package com.kasir.application.exception;


public class InternalErrorException extends RuntimeException {
    public InternalErrorException(String message) {
        super(message);
    }
}

