package com.jasonpyau.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.User;
import com.jasonpyau.form.BlogSearchForm;
import com.jasonpyau.form.NewBlogForm;
import com.jasonpyau.form.PaginationForm;
import com.jasonpyau.repository.BlogRepository;
import com.jasonpyau.util.DateFormat;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class BlogService {

    @Autowired
    private BlogRepository blogRepository;
    @Autowired
    private UserService userService;

    public void newBlog(NewBlogForm newBlogForm) {
        Blog blog = Blog.builder()
                        .title(newBlogForm.getTitle())
                        .description(newBlogForm.getDescription())
                        .body(newBlogForm.getBody())
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

    public Page<Blog> getBlogs(HttpServletRequest request, BlogSearchForm blogSearchForm, PaginationForm paginationForm) {
        Direction direction = (blogSearchForm.getAscending()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, blogSearchForm.getOrderBy());
        Pageable pageable = PageRequest.of(paginationForm.getPageNum(), paginationForm.getPageSize(), sort);
        Page<Blog> page = (blogSearchForm.getLiked()) ? 
                            blogRepository.findAllLikedWithPagination(pageable, UserService.getUserAddress(request), blogSearchForm.getSearch()) :
                            blogRepository.findAllWithPagination(pageable, blogSearchForm.getSearch());
        List<Blog> blogs = page.getContent();
        if (blogSearchForm.getLiked()) {
            for (Blog blog : blogs) {
                blog.setIsLikedByUser(true);
            }
        } else {
            User dummyUser = userService.getDummyUser(request);
            for (Blog blog : blogs) {
                blog.checkIsLikedByUser(dummyUser);
            }
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

    public HashMap<String, Long> getPrevAndNext(HttpServletRequest request, BlogSearchForm blogSearchForm, Long id) {
        Direction direction = (blogSearchForm.getAscending()) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Sort sort = Sort.by(direction, blogSearchForm.getOrderBy());
        Pageable pageable = PageRequest.of(0, Integer.MAX_VALUE, sort);
        Page<Blog> page = (blogSearchForm.getLiked()) ? 
                            blogRepository.findAllLikedWithPagination(pageable, UserService.getUserAddress(request), blogSearchForm.getSearch()) :
                            blogRepository.findAllWithPagination(pageable, blogSearchForm.getSearch());
        List<Blog> blogs = page.getContent();
        Optional<Blog> optional = blogRepository.findById(id);
        if (!optional.isPresent()) {
            return null;
        }
        Blog blog = optional.get();
        int lower = search(blogs, blog, blogSearchForm.getAscending(), blogSearchForm.getOrderBy(), true);
        if (lower == -1) {
            return null;
        }
        int higher = search(blogs, blog, blogSearchForm.getAscending(), blogSearchForm.getOrderBy(), false);
        int index;
        for (index = lower; index <= higher; index++) {
            if (blogs.get(index).equals(blog)) {
                break;
            }
        }
        Long prev = null, next = null;
        if (index >= 1 && index <= blogs.size()-1) {
            prev = blogs.get(index-1).getId();
        }
        if (index >= 0 && index <= blogs.size()-2) {
            next = blogs.get(index+1).getId();
        }
        HashMap<String, Long> res = new HashMap<>();
        res.put("prev", prev);
        res.put("next", next);
        return res;
    }

    // Get lower/upper bound of blogs in "blogs" that have the same value in the field regarding "orderBy" as "rhs" 
    private int search(List<Blog> blogs, Blog rhs, boolean ascending, String orderBy, boolean lowerBound) {
        int ans = -1, asc = (ascending) ? -1 : 1;
        int low = 0, mid, high = blogs.size()-1;
        while (low <= high) {
            mid = (int)((0L+low+high))>>1;
            int compare;
            Blog lhs = blogs.get(mid);
            switch (orderBy) {
                case "title":
                    compare = asc*lhs.getTitle().compareTo(rhs.getTitle());
                    break;
                case "like_count":
                    compare = asc*lhs.getLikeCount().compareTo(rhs.getLikeCount());
                    break;
                case "view_count":
                    compare = asc*lhs.getViewCount().compareTo(rhs.getViewCount());
                    break;
                case "unix_time":
                default:
                    compare = asc*lhs.getUnixTime().compareTo(rhs.getUnixTime());
            }
            if (compare == 0) {
                ans = mid;
                if (lowerBound) {
                    high = mid-1;
                } else {
                    low = mid+1;
                }
            } else if (compare > 0) {
                low = mid+1;
            } else {
                high = mid-1;
            }            
        }
        return ans;
    }

}
