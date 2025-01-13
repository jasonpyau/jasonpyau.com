package com.jasonpyau.controller;

import java.io.IOException;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Blog;
import com.jasonpyau.entity.Metadata;
import com.jasonpyau.form.BlogSearchForm;
import com.jasonpyau.service.AboutMeService;
import com.jasonpyau.service.BlogService;
import com.jasonpyau.service.LinkService;
import com.jasonpyau.service.MetadataService;
import com.jasonpyau.util.NumberFormat;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
public class FrontendController {

    @Autowired
    private MetadataService metadataService;
    @Autowired
    private BlogService blogService;
    @Autowired
    private AboutMeService aboutMeService;
    @Autowired
    private LinkService linkService;
    
    @GetMapping("/")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String home(HttpServletRequest request, Model model) {
        addMetadata(model);
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
        addMetadata(model);
        model.addAttribute("links", linkService.getLinks());
        return "links";
    }

    @GetMapping({"/blogs", "/blogs/"})
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String blogs(HttpServletRequest request, Model model) {
        addMetadata(model);
        return "blogs";
    }

    @GetMapping({"/blogs/{id}", "blogs/{id}/"})
    @RateLimit(RateLimit.BIG_TOKEN)
    public String blog(@PathVariable("id") Long id, HttpServletRequest request, Model model, @Valid BlogSearchForm blogSearchForm) {
        addMetadata(model);
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
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    public String resume(HttpServletRequest request) throws IOException {
        String resumeLink = metadataService.getMetadata().getResumeLink();
        if (StringUtils.hasText(resumeLink)) {
            return "redirect:"+resumeLink;
        }
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        ((AntPathMatcher)resolver.getPathMatcher()).setCaseSensitive(false);
        Resource[] resources = resolver.getResources("classpath*:/static/files/*RESUME*.pdf");
        if (resources.length > 0) {
            return "redirect:/files/"+resources[0].getFilename();
        }
        return "error";
    }

    private void addMetadata(Model model) {
        Metadata metadata = metadataService.updateViews();
        if (metadata == null) {
            return;
        }
        model.addAttribute(metadata);
        model.addAttribute("formattedViews", NumberFormat.shorten(metadata.getViews()));
    }
}
