package com.jasonpyau.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.jasonpyau.entity.Stats;
import com.jasonpyau.service.RateLimitService;
import com.jasonpyau.service.StatsService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class FrontendController {

    @Autowired
    private StatsService statsService;
    
    @GetMapping("/")
    public String home(HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        return "index";
    }

    @GetMapping("/index")
    public String index(HttpServletRequest request, Model model) {
        return home(request, model);
    }

    @GetMapping("/links")
    public String links(HttpServletRequest request, Model model) {
        if (RateLimitService.rateLimitService.rateLimit(request)) {
            return "ratelimit";
        }
        updateStats(model);
        return "links";
    }

    @GetMapping("/projects")
    public String projects() {
        return "projects";
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
