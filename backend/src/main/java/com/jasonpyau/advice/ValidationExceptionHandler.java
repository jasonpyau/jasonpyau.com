package com.jasonpyau.advice;

import java.util.HashMap;
import java.util.HashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.util.Response;

import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ResponseEntity<HashMap<String, Object>> res = Response.notAcceptable();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> body = res.getBody();
        HashMap<String, String> errorMap = new HashMap<>();
        HashSet<String> set = new HashSet<>();
        body.put("invalidFields", errorMap);
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errorMap.put(error.getField(), error.getDefaultMessage());
            if (!set.contains(error.getDefaultMessage())) {
                sb.append(error.getDefaultMessage()+' ');
                set.add(error.getDefaultMessage());
            }
        });
        if (sb.length() > 0) {
            sb.setLength(sb.length()-1);
        }
        body.put("reason", sb.toString());
        return res;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HashMap<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        ResponseEntity<HashMap<String, Object>> res = Response.notAcceptable();
        HashMap<String, Object> body = res.getBody();
        HashSet<String> set = new HashSet<>();
        StringBuilder sb = new StringBuilder();
        e.getConstraintViolations().forEach(error -> {
            if (!set.contains(error.getMessage())) {
                sb.append(error.getMessage()+' ');
                set.add(error.getMessage());
            }
        });
        if (sb.length() > 0) {
            sb.setLength(sb.length()-1);
        }
        body.put("reason", sb.toString());
        return res;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<HashMap<String, Object>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        Throwable cause = e.getCause();
        if (cause instanceof InvalidFormatException) {
            InvalidFormatException invalidFormatException = (InvalidFormatException)cause;
            if (invalidFormatException.getTargetType().equals(Skill.Type.class)) {
                return Response.errorMessage(Skill.SKILL_TYPE_ERROR, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return Response.errorMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<HashMap<String, Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Response.errorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
