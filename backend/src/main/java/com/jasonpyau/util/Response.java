package com.jasonpyau.util;

import java.util.HashMap;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class Response {
    
    private Response() {};

    public static HashMap<String, Object> createBody(String key, Object value) {
        return createBody(new String[] {key}, new Object[] {value});
    }

    public static HashMap<String, Object> createBody(String[] keys, Object[] values) {
        if (keys.length != values.length)
            return null;
        HashMap<String, Object> res = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            res.put(keys[i], values[i]);
        }
        return res;
    }

    public static ResponseEntity<HashMap<String, Object>> rateLimit() {
        return new ResponseEntity<>(HttpStatus.TOO_MANY_REQUESTS);
    }
}
