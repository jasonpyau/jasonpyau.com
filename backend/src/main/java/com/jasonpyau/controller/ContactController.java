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
import com.jasonpyau.util.Response;

@Controller
@RequestMapping(path="/contact")
public class ContactController {
    
    @Autowired
    private ContactService contactService;
    
    @PutMapping(path = "/send", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> sendMessage(@RequestBody(required = true) Message message) {
        String errorMessage = contactService.sendMessage(message);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody("status", "success"), HttpStatus.OK);
    }

    @GetMapping(path = "/get", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getMessages(@RequestBody GetMesssagesForm getMesssagesForm) {
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