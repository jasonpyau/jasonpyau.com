package com.jasonpyau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.jasonpyau.service.AuthorizationService;
import com.jasonpyau.service.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShutDownController {

    @DeleteMapping(path = "/shut_down")
    @CrossOrigin
    public void shutDown(HttpServletRequest request) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return;
        }
        if (!AuthorizationService.authorize(request)) {
            return;
        }
        System.exit(0);
    }
}
