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
import com.jasonpyau.entity.Blog;
import com.jasonpyau.form.BlogSearchForm;
import com.jasonpyau.form.NewBlogForm;
import com.jasonpyau.form.PaginationForm;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@Validated
@RequestMapping(path="/blogs")
public class BlogController {
    
    @Autowired
    private BlogService blogService;

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @AuthorizeAdmin
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newBlog(HttpServletRequest request, @Valid @RequestBody NewBlogForm newBlogForm) {
        blogService.newBlog(newBlogForm);
        return Response.success();
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @AuthorizeAdmin
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        blogService.deleteBlog(id);
        return Response.success();
    }

    @GetMapping(path = "/get/page", produces = "application/json")
    @RateLimit(RateLimit.BIG_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getBlog(HttpServletRequest request,
                                                    @Valid BlogSearchForm blogSearchForm,
                                                    @Valid PaginationForm paginationForm) {
        Page<Blog> page = blogService.getBlogs(request, blogSearchForm, paginationForm);
        String[] keys = {"blogs", "totalPages", "hasNext"};
        Object[] values = {page.getContent(), page.getTotalPages(), page.hasNext()};
        return Response.success(Response.createBody(keys, values));
    }

    @PostMapping(path = "/like/{id}", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> likeBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        blogService.like(request, id);
        return Response.success();
    }

    @PostMapping(path = "/unlike/{id}", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> unlikeBlog(HttpServletRequest request, @PathVariable("id") Long id) {
        blogService.unlike(request, id);
        return Response.success();
    }
    
}
