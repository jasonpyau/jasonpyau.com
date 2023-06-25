package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonpyau.entity.Message;
import com.jasonpyau.service.AuthorizationService;
import com.jasonpyau.service.ContactService;
import com.jasonpyau.service.RateLimitService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/contact")
public class ContactController {
    
    @Autowired
    private ContactService contactService;
    private RateLimitService sendMessageRateLimitService = new RateLimitService(RateLimitService.SEND_MESSAGES_TYPE);
    
    @PutMapping(path = "/send", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> sendMessage(HttpServletRequest request, @RequestBody(required = true) Message message) {
        if (sendMessageRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        String errorMessage = contactService.sendMessage(message);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteMessage(HttpServletRequest request, @PathVariable("id") Long id) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = contactService.deleteMessage(id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getMessages(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        if (pageNum == null || pageSize == null || pageSize <= 0 || pageSize > 50) {
            return Response.notAcceptable();
        }
        Page<Message> page = contactService.getMessages(pageNum, pageSize);
        String[] keys = {"messages", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return new ResponseEntity<>(Response.createBody(keys, values), HttpStatus.OK);
    }
}
