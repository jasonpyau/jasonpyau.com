package com.jasonpyau.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.entity.Message;
import com.jasonpyau.service.Authorization;
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
        return new ResponseEntity<>(Response.createBody("status", "success"), HttpStatus.OK);
    }

    @GetMapping(path = "/get", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getMessages(HttpServletRequest request, @RequestBody(required = true) GetMesssagesForm getMesssagesForm) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        Authorization authorization = new Authorization();
        authorization.setPassword(getMesssagesForm.getPassword());
        if (!authorization.authorize()) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
        Integer pageNum = getMesssagesForm.getPageNum(), pageSize = getMesssagesForm.getPageSize();
        if (pageNum == null || pageSize == null || pageSize <= 0 || pageSize > 50) {
            return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
        }
        List<Message> messages = contactService.getMessages(pageNum, pageSize);
        return new ResponseEntity<>(Response.createBody("messages", messages), HttpStatus.OK);
    }
}

class GetMesssagesForm {
    private Integer pageNum;
    private Integer pageSize;
    private String password;

    public Integer getPageNum() {
        return pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public String getPassword() {
        return password;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}