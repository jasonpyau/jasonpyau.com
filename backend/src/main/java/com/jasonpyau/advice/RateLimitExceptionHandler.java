package com.jasonpyau.advice;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.jasonpyau.exception.RateLimitException;
import com.jasonpyau.util.Response;

@ControllerAdvice
public class RateLimitExceptionHandler {
    
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<HashMap<String, Object>> handleRateLimitException(RateLimitException e) {
        return Response.rateLimit(e.getMs());
    }
}
