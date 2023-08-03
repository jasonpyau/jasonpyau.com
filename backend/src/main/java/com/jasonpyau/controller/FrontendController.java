package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.Stats;
import com.jasonpyau.form.BlogSearchForm;
import com.jasonpyau.service.AboutMeService;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.service.StatsService;
import com.jasonpyau.util.NumberFormat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class FrontendController {

    @Autowired
    private StatsService statsService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AboutMeService aboutMeService;
    
    @GetMapping("/")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String home(HttpServletRequest request, Model model) {
        updateStats(model);
        model.addAttribute("aboutMe", aboutMeService.getAboutMe());
        return "index";
    }

    @GetMapping({"/index", "/index/"})
    public String index(HttpServletRequest request, Model model) {
        return home(request, model);
    }

    @GetMapping({"/links", "/links/"})
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String links(HttpServletRequest request, Model model) {
        updateStats(model);
        return "links";
    }

    @GetMapping({"/blogs", "/blogs/"})
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String blogs(HttpServletRequest request, Model model) {
        updateStats(model);
        return "blogs";
    }

    @GetMapping({"/blogs/{id}", "blogs/{id}/"})
    @RateLimit(RateLimit.BIG_TOKEN)
    public String blog(@PathVariable("id") Long id, HttpServletRequest request, Model model, @Valid BlogSearchForm blogSearchForm) {
        updateStats(model);
        HashMap<String, Blog> res = blogService.getBlog(request, id, blogSearchForm);
        if (res == null) {
            return "error";
        }
        Blog blog = res.get("blog");
        model.addAttribute("blog", blog);
        model.addAttribute("prev", res.get("prev"));
        model.addAttribute("next", res.get("next"));
        model.addAttribute("queryString", (request.getQueryString() == null) ? "" : "?"+request.getQueryString());
        String bodyHTML = String.format("<div class=\"fs-3 fw-bold fst-italic text-decoration-underline text-center\">%s</div>" +
                                                "<br><br> "+
                                                "<div class=\"fs-4 fw-semibold text-left\">%s</div>", blog.getDescription(), blog.getBody());
        model.addAttribute("bodyHTML", bodyHTML);
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
        model.addAttribute("views", NumberFormat.shorten(stats.getViews()));
        model.addAttribute("lastUpdated", stats.getDate());
    }
}
