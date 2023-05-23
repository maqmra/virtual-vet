package com.example.virtualvet.controller.errors;

import com.example.virtualvet.exception.NullReferenceException;
import com.example.virtualvet.exception.ResourceAlreadyExistsException;
import com.example.virtualvet.exception.ResourceNotFoundException;
import org.hibernate.PropertyValueException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.Instant;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice()
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<Object> handleSqlResourceAlreadyExistsException(ResourceAlreadyExistsException exception) {
        return new ResponseEntity<>(new ErrorResponse(Instant.now(), exception.getMessage(), HttpStatus.CONFLICT), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFoundException(ResourceNotFoundException exception) {
        return new ResponseEntity<>(new ErrorResponse(Instant.now(), exception.getMessage(), HttpStatus.NOT_FOUND), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NullReferenceException.class)
    public ResponseEntity<Object> handleNullReferenceException(NullReferenceException exception) {
        return new ResponseEntity<>(new ErrorResponse(Instant.now(), exception.getMessage(), HttpStatus.FORBIDDEN), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception exception) {
        return new ResponseEntity<>(new ErrorResponse(Instant.now(), exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private record ErrorResponse(Instant timestamp, String message, HttpStatus status) { // TODO: read about record
    }
}
