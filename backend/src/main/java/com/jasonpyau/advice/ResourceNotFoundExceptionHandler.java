package com.jasonpyau.advice;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.util.Response;

@ControllerAdvice
public class ResourceNotFoundExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<HashMap<String, Object>> handleResourceNotFoundException(ResourceNotFoundException e) {
        return Response.errorMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }
}
