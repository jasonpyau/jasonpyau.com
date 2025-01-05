package com.jasonpyau.controller;

import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jasonpyau.annotation.AuthorizeAdmin;
import com.jasonpyau.annotation.RateLimit;
import com.jasonpyau.entity.Experience;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.service.ExperienceService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Controller
@Validated
@RequestMapping(path = "/experiences")
public class ExperienceController {
    
    @Autowired
    private ExperienceService experienceService;

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newExperience(HttpServletRequest request, @Valid @RequestBody Experience experience) {
        experienceService.newExperience(experience);
        return Response.success();
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateExperience(HttpServletRequest request, @RequestBody Experience updateExperience, @PathVariable("id") Integer id) {
        experienceService.updateExperience(updateExperience, id);
        return Response.success();
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteExperience(HttpServletRequest request, @PathVariable("id") Integer id) {
        experienceService.deleteExperience(id);
        return Response.success();
    }

    @PostMapping(path = "{id}/skills/new", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newExperienceSkill(HttpServletRequest request, 
                                                                    @PathVariable("id") Integer id, 
                                                                    @RequestParam(required = true) @Size(min = 1, max = 25, message = Skill.SKILL_NAME_ERROR) String skillName) {
        experienceService.newExperienceSkill(skillName, id);
        return Response.success();
    }

    @DeleteMapping(path = "{id}/skills/delete", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteExperienceSkill(HttpServletRequest request, 
                                                                    @PathVariable("id") Integer id, 
                                                                    @RequestParam(required = true) @Size(min = 1, max = 25, message = Skill.SKILL_NAME_ERROR) String skillName) {
        experienceService.deleteExperienceSkill(skillName, id);
        return Response.success();
    }

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getExperiences(HttpServletRequest request) {
        return Response.success(Response.createBody("experiences", experienceService.getExperiences()));
    }

    @GetMapping(path = "/valid_types", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> validTypes(HttpServletRequest request) {
        return Response.success(Response.createBody("validTypes", experienceService.validTypes()));
    }
}
