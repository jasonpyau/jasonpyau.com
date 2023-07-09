package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.entity.Blog;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.service.RateLimitService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Controller
@Validated
@RequestMapping(path="/blogs")
public class BlogController {
    
    @Autowired
    private BlogService blogService;

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newBlog(HttpServletRequest request, @Valid @RequestBody NewBlogForm newBlogForm) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        blogService.newBlog(newBlogForm.getTitle(), newBlogForm.getBody());
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @AuthorizeAdmin
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        if (RateLimitService.adminRateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        String errorMessage = blogService.deleteBlog(id);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get/page", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getBlog(HttpServletRequest request, 
                                                    @RequestParam(defaultValue = "0") @Min(value = 0, message = Blog.BLOG_PAGE_NUM_ERROR) Integer pageNum, 
                                                    @RequestParam(defaultValue = "5") @Min(value = 1, message = Blog.BLOG_PAGE_SIZE_ERROR) @Max(value = 50, message = Blog.BLOG_PAGE_SIZE_ERROR) Integer pageSize) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
        }
        Page<Blog> page = blogService.getBlogs(request, pageNum, pageSize);
        String[] keys = {"blogs", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return new ResponseEntity<>(Response.createBody(keys, values), HttpStatus.OK);
    }

    @GetMapping(path = "/get/page/liked", produces = "application/json")
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getLikedBlogs(HttpServletRequest request, 
                                                    @RequestParam(defaultValue = "0") @Min(value = 0, message = Blog.BLOG_PAGE_NUM_ERROR) Integer pageNum, 
                                                    @RequestParam(defaultValue = "5") @Min(value = 1, message = Blog.BLOG_PAGE_SIZE_ERROR) @Max(value = 50, message = Blog.BLOG_PAGE_SIZE_ERROR) Integer pageSize) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return Response.rateLimit();
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
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
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
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

}

@Getter
@AllArgsConstructor
class NewBlogForm {

    @Size(min = 3, max = 250, message = Blog.BLOG_TITLE_ERROR)
    @NotBlank(message = Blog.BLOG_TITLE_ERROR)
    private String title;
    @Size(max = 5000, message = Blog.BLOG_BODY_ERROR)
    @NotBlank(message = Blog.BLOG_BODY_ERROR)
    private String body;

}

