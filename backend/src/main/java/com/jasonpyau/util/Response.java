package com.jasonpyau.util;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    
    private Response() {};

    public static HashMap<String, Object> createBody() {
        return createBody(new String[] {}, new Object[] {});
    }

    public static HashMap<String, Object> createBody(String key, Object value) {
        return createBody(new String[] {key}, new Object[] {value});
    }

    public static HashMap<String, Object> createBody(String[] keys, Object[] values) {
        if (keys.length != values.length)
            return null;
        HashMap<String, Object> res = new HashMap<>();
        res.put("status", "success");
        for (int i = 0; i < keys.length; i++) {
            res.put(keys[i], values[i]);
        }
        return res;
    }

    public static ResponseEntity<HashMap<String, Object>> rateLimit() {
        return new ResponseEntity<>(createBody("status", "Rate Limit, try again later."), HttpStatus.TOO_MANY_REQUESTS);
    }

    public static ResponseEntity<HashMap<String, Object>> serverError() {
        return new ResponseEntity<>(createBody("status", "Server Error."), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<HashMap<String, Object>> unauthorized() {
        return new ResponseEntity<>(createBody("status", "Unauthorized Request, please give valid credentials."), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HashMap<String, Object>> notAcceptable() {
        return new ResponseEntity<>(createBody("status", "Invalid request, bad input."), HttpStatus.NOT_ACCEPTABLE);
    }
}
