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

    public static ResponseEntity<HashMap<String, Object>> rateLimit(Long ms) {
        String[] keys = {"status", "reason"};
        String[] values = {"error", "Rate Limit, try again in "+ms+"ms."};
        return new ResponseEntity<>(createBody(keys, values), HttpStatus.TOO_MANY_REQUESTS);
    }

    public static ResponseEntity<HashMap<String, Object>> serverError() {
        String[] keys = {"status", "reason"};
        String[] values = {"error", "Server Error."};
        return new ResponseEntity<>(createBody(keys, values), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static ResponseEntity<HashMap<String, Object>> unauthorized() {
        String[] keys = {"status", "reason"};
        String[] values = {"error", "Unauthorized Request, please give valid credentials."};
        return new ResponseEntity<>(createBody(keys, values), HttpStatus.UNAUTHORIZED);
    }

    public static ResponseEntity<HashMap<String, Object>> notAcceptable() {
        String[] keys = {"status", "reason"};
        String[] values = {"error", "Invalid request, bad input."};
        return new ResponseEntity<>(createBody(keys, values), HttpStatus.NOT_ACCEPTABLE);
    }

    public static ResponseEntity<HashMap<String, Object>> errorMessage(String reason, HttpStatus status) {
        String[] keys = {"status", "reason"};
        String[] values = {"error", reason};
        return new ResponseEntity<>(createBody(keys, values), status);
    }
}
