package com.jasonpyau.advice;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jasonpyau.exception.ResourceAlreadyExistsException;
import com.jasonpyau.util.Response;

@ControllerAdvice
public class ResourceAlreadyExistsExceptionHandler {
    
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<HashMap<String, Object>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        return Response.errorMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
