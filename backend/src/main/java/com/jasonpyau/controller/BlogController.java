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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonpyau.entity.Blog;
import com.jasonpyau.service.AuthorizationService;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.service.RateLimitService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Getter;
import lombok.Setter;

@Controller
@RequestMapping(path="/blogs")
public class BlogController {
    
    @Autowired
    private BlogService blogService;

    @PutMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newBlog(HttpServletRequest request, @RequestBody(required = true) NewBlogForm newBlogForm) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = blogService.newBlog(newBlogForm.getTitle(), newBlogForm.getBody());
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (!AuthorizationService.authorize(request)) {
            return Response.unauthorized();
        }
        String errorMessage = blogService.deleteBlog(id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get/page", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getBlog(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "5") Integer pageSize) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (pageNum == null || pageSize == null || pageSize <= 0 || pageSize > 50) {
            return Response.notAcceptable();
        }
        Page<Blog> page = blogService.getBlogs(request, pageNum, pageSize);
        String[] keys = {"blogs", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return new ResponseEntity<>(Response.createBody(keys, values), HttpStatus.OK);
    }

    @GetMapping(path = "/get/page/liked", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getLikedBlogs(HttpServletRequest request, @RequestParam(defaultValue = "0") Integer pageNum, @RequestParam(defaultValue = "10") Integer pageSize) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        if (pageNum == null || pageSize == null || pageSize <= 0 || pageSize > 50) {
            return Response.notAcceptable();
        }
        Page<Blog> page = blogService.getLikedBlogs(request, pageNum, pageSize);
        String[] keys = {"blogs", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return new ResponseEntity<>(Response.createBody(keys, values), HttpStatus.OK);
    }

    @PostMapping(path = "/like/{id}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> likeBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        String errorMessage = blogService.like(request, id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @PostMapping(path = "/unlike/{id}", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> unlikeBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        String errorMessage = blogService.unlike(request, id);
        if (errorMessage != null) {
            return new ResponseEntity<>(Response.createBody("status", errorMessage), HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

}

@Getter
@Setter
class NewBlogForm {

    private String title;
    private String body;

}

