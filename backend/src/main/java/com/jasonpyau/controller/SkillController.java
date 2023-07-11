package com.jasonpyau.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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
        String errorMessage = skillService.newSkill(skill);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{name}", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteSkill(HttpServletRequest request, @PathVariable("name") String skillName) {
        String errorMessage = skillService.deleteSkill(skillName);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getSkills(HttpServletRequest request) {
        HashMap<String, List<Skill>> skills = skillService.getSkills();
        return new ResponseEntity<>(Response.createBody("skills", skills), HttpStatus.OK);
    }

    @GetMapping(path = "/valid_types", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> validTypes(HttpServletRequest request) {
        List<String> validTypes = skillService.validTypes();
        return new ResponseEntity<>(Response.createBody("validTypes", validTypes), HttpStatus.OK);
    }
    
}
