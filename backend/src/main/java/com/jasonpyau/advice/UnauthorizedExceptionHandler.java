package com.jasonpyau.advice;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jasonpyau.exception.UnauthorizedException;
import com.jasonpyau.util.Response;

@ControllerAdvice
public class UnauthorizedExceptionHandler {
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<HashMap<String, Object>> handleUnauthorizedException(UnauthorizedException e) {
        return Response.unauthorized();
    }
}
