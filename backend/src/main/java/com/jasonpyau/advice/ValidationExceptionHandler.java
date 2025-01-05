package com.jasonpyau.advice;

import java.util.Comparator;
import java.util.HashMap;
import java.util.TreeMap;
import java.util.TreeSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.entity.Skill.SkillType;
import com.jasonpyau.util.Response;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@ControllerAdvice
public class ValidationExceptionHandler {
    
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<HashMap<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        ResponseEntity<HashMap<String, Object>> res = Response.notAcceptable();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> body = res.getBody();
        TreeMap<String, String> errorMap = new TreeMap<>();
        body.put("invalidFields", errorMap);
        TreeSet<FieldError> set = new TreeSet<>(Comparator.comparing(FieldError::getField));
        set.addAll(e.getBindingResult().getFieldErrors());
        for (FieldError fieldError : set) {
            errorMap.put(fieldError.getField(), fieldError.getDefaultMessage());
            sb.append(fieldError.getDefaultMessage()+' ');
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length()-1);
        }
        body.put("reason", sb.toString());
        return res;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<HashMap<String, Object>> handleConstraintViolationException(ConstraintViolationException e) {
        ResponseEntity<HashMap<String, Object>> res = Response.notAcceptable();
        StringBuilder sb = new StringBuilder();
        HashMap<String, Object> body = res.getBody();
        TreeMap<String, String> errorMap = new TreeMap<>();
        body.put("invalidFields", errorMap);
        TreeSet<ConstraintViolation<?>> set = new TreeSet<>((a, b) -> a.getPropertyPath().toString().compareTo(b.getPropertyPath().toString()));
        set.addAll(e.getConstraintViolations());
        for (ConstraintViolation<?> constraintViolation : set) {
            errorMap.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage());
            sb.append(constraintViolation.getMessage()+' ');
        }
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
            if (invalidFormatException.getTargetType().equals(SkillType.class)) {
                return Response.errorMessage(Skill.SKILL_TYPE_ERROR, HttpStatus.NOT_ACCEPTABLE);
            }
        }
        return Response.errorMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<HashMap<String, Object>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Response.errorMessage(e.getMessage(), HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<HashMap<String, Object>> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return Response.errorMessage(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
