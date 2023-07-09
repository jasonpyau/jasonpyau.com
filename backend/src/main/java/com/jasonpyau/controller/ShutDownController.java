package com.jasonpyau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.service.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShutDownController {

    @DeleteMapping(path = "/shut_down")
    @AuthorizeAdmin
    @CrossOrigin
    public void shutDown(HttpServletRequest request) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return;
        }
        System.exit(0);
    }
}
