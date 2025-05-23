package com.jasonpyau.controller;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.service.SkillService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@Controller
@RequestMapping(path = "/skills")
public class SkillController {

    @Autowired
    private SkillService skillService;

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newSkill(HttpServletRequest request, @Valid @RequestBody Skill skill) {
        skillService.newSkill(skill);
        return Response.success();
    }

    @PatchMapping(path = "/update", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateSkill(HttpServletRequest request, @RequestBody Skill skill) {
        skillService.updateSkill(skill);
        return Response.success();
    }


    @DeleteMapping(path = "/delete/{name}", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteSkill(HttpServletRequest request, @PathVariable("name") String skillName) {
        skillService.deleteSkill(skillName);
        return Response.success();
    }

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getSkills(HttpServletRequest request) {
        return Response.success(Response.createBody("skills", skillService.getSkills()));
    }

    @GetMapping(path = "/svg/{name}", produces = "image/svg+xml")
    @RateLimit(RateLimit.CHEAP_TOKEN)
    @CrossOrigin
    public ResponseEntity<String> getSkillIconSvg(HttpServletRequest request, @PathVariable("name") String skillName) {
        return ResponseEntity.ok()
                            .cacheControl(CacheControl.maxAge(30, TimeUnit.MINUTES))
                            .body(skillService.getSkillIconSvg(skillName));
    }

    @GetMapping(path = "/valid_types", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> validTypes(HttpServletRequest request) {
        return Response.success(Response.createBody("validTypes", skillService.validTypes()));
    }
    
}
