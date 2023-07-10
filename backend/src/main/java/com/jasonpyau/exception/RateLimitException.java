package com.jasonpyau.exception;

import lombok.Getter;

public class RateLimitException extends Exception {
    
    @Getter
    private long ms;

    public RateLimitException(long ms) {
        super();
        this.ms = ms;
    }
}
