package com.jasonpyau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.Stats;
import com.jasonpyau.service.AboutMeService;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.service.RateLimitService;
import com.jasonpyau.service.StatsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontendController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AboutMeService aboutMeService;
    
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        model.addAttribute("aboutMe", aboutMeService.getAboutMe());
        return "index";
    }

    @GetMapping({"/index", "/index/"})
    public String index(HttpServletRequest request, Model model) {
        return home(request, model);
    }

    @GetMapping({"/links", "/links/"})
    public String links(HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        return "links";
    }

    @GetMapping({"/projects","/projects/"})
    public String projects() {
        return "projects";
    }

    @GetMapping({"/blogs", "/blogs/"})
    public String blogs(HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        return "blogs";
    }

    @GetMapping({"/blogs/{id}", "blogs/{id}/"})
    public String blog(@PathVariable("id") Long id, HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        Blog blog = blogService.getBlog(request, id);
        if (blog == null) {
            return "error";
        }
        model.addAttribute("id", blog.getId());
        model.addAttribute("title", blog.getTitle());
        model.addAttribute("body", blog.getBody());
        model.addAttribute("date", blog.getDate());
        model.addAttribute("blogViewCount", blog.getViewCount());
        model.addAttribute("blogLikeCount", blog.getLikeCount());
        model.addAttribute("isLikedByUser", blog.getIsLikedByUser());
        return "blog";
    }

    @GetMapping({"/resume", "/resume/"})
    public String resume() {
        return "redirect:/files/Jason_Yau_Resume.pdf";
    }

    private void updateStats(Model model) {
        Stats stats = statsService.updateViews();
        if (stats == null) {
            return;
        }
        model.addAttribute("views", stats.getViews());
        model.addAttribute("lastUpdated", stats.getDate());
    }
}
