package com.kasir.application.exception;

import com.kasir.application.payload.response.ResponseHelper;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<?> notFound(ChangeSetPersister.NotFoundException notFoundException) {
        return ResponseHelper.error(notFoundException.getMessage(), HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(InternalErrorException.class)
    public ResponseEntity<?> InternalError(InternalErrorException internalErrorException) {
        return ResponseHelper.error(internalErrorException.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}


