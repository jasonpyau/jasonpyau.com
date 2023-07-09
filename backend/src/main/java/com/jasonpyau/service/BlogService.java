package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.User;
import com.jasonpyau.repository.BlogRepository;
import com.jasonpyau.util.DateFormat;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserService userService;

    public void newBlog(String title, String body) {
        Blog blog = Blog.builder()
                        .title(title)
                        .body(body)
                        .date(DateFormat.MMddyyyy())
                        .unixTime(DateFormat.getUnixTime())
                        .likeCount(0)
                        .viewCount(0L)
                        .build();
        blogRepository.save(blog);
    }

    // Returns error message if applicable, else null.
    public String deleteBlog(Long id) {
        Optional<Blog> optional = blogRepository.findById(id);
        if (!optional.isPresent()) {
            return Blog.BLOG_ID_ERROR;
        }
        blogRepository.delete(optional.get());
        return null;
    }

    // Returns requested Blog if found, else null.
    public Blog getBlog(HttpServletRequest request, Long id) {
        Optional<Blog> optional = blogRepository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        Blog blog = optional.get();
        blog.view();
        User dummyUser = userService.getDummyUser(request);
        blog.checkIsLikedByUser(dummyUser);
        return blogRepository.save(blog);
    }

    public Page<Blog> getBlogs(HttpServletRequest request, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Blog> page = blogRepository.findAllWithPaginationOrderedByUnixTime(pageable);
        List<Blog> blogs = page.getContent();
        User dummyUser = userService.getDummyUser(request);
        for (Blog blog : blogs) {
            blog.checkIsLikedByUser(dummyUser);
        }
        return page;
    }

    public Page<Blog> getLikedBlogs(HttpServletRequest request, int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Blog> page = blogRepository.findLikedBlogsWithPaginationOrderedByUnixTime(pageable, UserService.getUserAddress(request));
        List<Blog> blogs = page.getContent();
        for (Blog blog : blogs) {
            blog.setIsLikedByUser(true);
        }
        return page;
    }

    // Returns error message if applicable, else null.
    public String like(HttpServletRequest request, Long id) {
        Optional<Blog> optional = blogRepository.findById(id);
        if (!optional.isPresent()) {
            return Blog.BLOG_ID_ERROR;
        }
        User user = userService.getUser(request);
        Blog blog = optional.get();
        blog.like(user);
        blogRepository.save(blog);
        return null;
    }

    // Returns error message if applicable, else null.
    public String unlike(HttpServletRequest request, Long id) {
        Optional<Blog> optional = blogRepository.findById(id);
        if (!optional.isPresent()) {
            return Blog.BLOG_ID_ERROR;
        }
        User user = userService.getUser(request);
        Blog blog = optional.get();
        blog.unlike(user);
        blogRepository.save(blog);
        return null;
    }

}
