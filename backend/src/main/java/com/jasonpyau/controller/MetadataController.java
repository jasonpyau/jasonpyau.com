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
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Metadata;
import com.jasonpyau.service.MetadataService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping(path="/metadata")
public class MetadataController {

    @Autowired
    private MetadataService metadataService;

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getMetadata(HttpServletRequest request) {
        Metadata metadata = metadataService.getMetadata();
        return new ResponseEntity<>(Response.createBody("metadata", metadata), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/views", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateViews(HttpServletRequest request) {
        Metadata metadata = metadataService.updateViews();
        return new ResponseEntity<>(Response.createBody("metadata", metadata), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/last_updated", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateLastUpdated(HttpServletRequest request) {
        Metadata metadata = metadataService.updateLastUpdated();
        return new ResponseEntity<>(Response.createBody("metadata", metadata), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/name", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateName(HttpServletRequest request, @RequestParam(required = true) String name) {
        Metadata metadata = metadataService.updateName(name);
        return new ResponseEntity<>(Response.createBody("metadata", metadata), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/icon_link", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateIconLink(HttpServletRequest request, @RequestParam(required = true) String iconLink) {
        Metadata metadata = metadataService.updateIconLink(iconLink);
        return new ResponseEntity<>(Response.createBody("metadata", metadata), HttpStatus.OK);
    }
}
