package com.jasonpyau.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.jasonpyau.service.RateLimitService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontendController {
    
    @GetMapping("/")
    public String home(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        return "index";
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request) {
        return home(request);
    }

    @GetMapping("/links")
    public String links(HttpServletRequest request) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        return "links";
    }

    @GetMapping("/header") 
    public String header() {
        return "header";
    }

    @GetMapping("/lastupdated")
    public String lastUpdated() {
        return "lastupdated";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
    }

    @GetMapping("/sourcecode")
    public String sourceCode() {
        return "sourcecode";
    }

    @GetMapping("/viewcount")
    public String viewCount() {
        return "viewcount";
    }
}
