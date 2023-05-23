package com.example.virtualvet.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NullReferenceException extends RuntimeException {
    public NullReferenceException(String message) {
        super(message);
    }
}
