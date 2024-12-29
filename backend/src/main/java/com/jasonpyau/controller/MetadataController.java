package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.form.MetadataUpdateForm;
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
        return Response.success(Response.createBody("metadata", metadataService.getMetadata()));
    }

    @PatchMapping(path = "/update/views", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateViews(HttpServletRequest request) {
        return Response.success(Response.createBody("metadata", metadataService.updateViews()));
    }

    @PatchMapping(path = "/update/last_updated", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateLastUpdated(HttpServletRequest request) {
        return Response.success(Response.createBody("metadata", metadataService.updateLastUpdated()));
    }

    @PatchMapping(path = "/update", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateWithForm(HttpServletRequest request, @RequestBody MetadataUpdateForm metadataUpdateForm) {
        return Response.success(Response.createBody("metadata", metadataService.updateWithForm(metadataUpdateForm)));
    }
}
