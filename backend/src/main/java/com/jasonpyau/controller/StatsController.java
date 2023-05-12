package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.entity.Stats;
import com.jasonpyau.service.Authorization;
import com.jasonpyau.service.StatsService;
import com.jasonpyau.util.Response;

@Controller
@RequestMapping(path="/stats")
public class StatsController {

    @Autowired
    private StatsService statsService;

    @GetMapping(path = "/get", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getStats() {
        Stats stats = statsService.getStats();
        HttpStatus status = (stats != null) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        HashMap<String, Object> body = Response.createBody("stats", stats);
        return new ResponseEntity<>(body, status);
    }

    @PostMapping(path = "/update/views", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateViews() {
        Stats stats = statsService.updateViews();
        HttpStatus status = (stats != null) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        HashMap<String, Object> body = Response.createBody("stats", stats);
        return new ResponseEntity<>(body, status);
    }

    @PostMapping(path = "/update/last_updated", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateLastUpdated(@RequestBody(required = true) Authorization authorization) {
        if (!authorization.authorize()) {
            return new ResponseEntity<>(Response.createBody("stats", null), HttpStatus.UNAUTHORIZED);
        }
        Stats stats = statsService.updateLastUpdated();
        HttpStatus status = (stats != null) ? HttpStatus.OK : HttpStatus.INTERNAL_SERVER_ERROR;
        HashMap<String, Object> body = Response.createBody("stats", stats);
        return new ResponseEntity<>(body, status);
    }
}
