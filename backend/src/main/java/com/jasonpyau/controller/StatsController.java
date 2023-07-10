package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Stats;
import com.jasonpyau.service.StatsService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getStats(HttpServletRequest request) {
        Stats stats = statsService.getStats();
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/views", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateViews(HttpServletRequest request) {
        Stats stats = statsService.updateViews();
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/last_updated", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateLastUpdated(HttpServletRequest request) {
        Stats stats = statsService.updateLastUpdated();
        return new ResponseEntity<>(Response.createBody("stats", stats), HttpStatus.OK);
    }
}
