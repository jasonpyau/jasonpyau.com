package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Message;
import com.jasonpyau.form.PaginationForm;
import com.jasonpyau.service.ContactService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@Validated
@RequestMapping(path="/contact")
public class ContactController {
    
    @Autowired
    private ContactService contactService;
    
    @PostMapping(path = "/send", consumes = "application/json", produces = "application/json")
    @RateLimit(RateLimit.EXPENSIVE_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> sendMessage(HttpServletRequest request, @Valid @RequestBody Message message) {
        contactService.sendMessage(request, message);
        return Response.success();
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteMessage(HttpServletRequest request, @PathVariable("id") Long id) {
        contactService.deleteMessage(id);
        return Response.success();
    }

    @GetMapping(path = "/get", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getMessages(HttpServletRequest request, @Valid PaginationForm paginationForm) {
        Page<Message> page = contactService.getMessages(paginationForm);
        String[] keys = {"messages", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return Response.success(Response.createBody(keys, values));
    }
}
