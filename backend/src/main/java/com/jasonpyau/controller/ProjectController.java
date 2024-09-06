package com.jasonpyau.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.jasonpyau.entity.Project;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.service.ProjectService;
import com.jasonpyau.util.Response;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

@Controller
@Validated
@RequestMapping(path = "/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;

    @PostMapping(path = "/new", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newProject(HttpServletRequest request, @Valid @RequestBody Project project) {
        projectService.newProject(project);
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @PatchMapping(path = "/update/{id}", consumes = "application/json", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> updateProject(HttpServletRequest request, @RequestBody Project updateProject, @PathVariable("id") Integer id) {
        String errorMessage = projectService.updateProject(updateProject, id);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "/delete/{id}", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteProject(HttpServletRequest request, @PathVariable("id") Integer id) {
        String errorMessage = projectService.deleteProject(id);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @PostMapping(path = "{id}/skills/new", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> newProjectSkill(HttpServletRequest request, 
                                                                    @PathVariable("id") Integer id, 
                                                                    @RequestParam(required = true) @Size(min = 1, max = 25, message = Skill.SKILL_NAME_ERROR) String skillName) {
        String errorMessage = projectService.newProjectSkill(skillName, id);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @DeleteMapping(path = "{id}/skills/delete", produces = "application/json")
    @AuthorizeAdmin
    @RateLimit(RateLimit.ADMIN_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> deleteProjectSkill(HttpServletRequest request, 
                                                                    @PathVariable("id") Integer id, 
                                                                    @RequestParam(required = true) @Size(min = 1, max = 25, message = Skill.SKILL_NAME_ERROR) String skillName) {
        String errorMessage = projectService.deleteProjectSkill(skillName, id);
        if (errorMessage != null) {
            return Response.errorMessage(errorMessage, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(Response.createBody(), HttpStatus.OK);
    }

    @GetMapping(path = "/get", produces = "application/json")
    @RateLimit(RateLimit.DEFAULT_TOKEN)
    @CrossOrigin
    public ResponseEntity<HashMap<String, Object>> getProjects(HttpServletRequest request) {
        List<Project> projects = projectService.getProjects();
        return new ResponseEntity<>(Response.createBody("projects", projects), HttpStatus.OK);
    }

}