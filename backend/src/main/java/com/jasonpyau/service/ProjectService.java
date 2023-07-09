package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Project;
import com.jasonpyau.repository.ProjectRepository;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    public void newProject(Project project) {
        project.createOrder();
        projectRepository.save(project);
    }

    // Returns error message if applicable, else null.
    public String updateProject(Project updateProject, Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        Project project = optional.get();
        Patch.merge(updateProject, project, "id", "dateOrder");
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        newProject(project);
        return null;
    }

    // Returns error message if applicable, else null.
    public String deleteProject(Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        projectRepository.delete(optional.get());
        return null;
    }

    public List<Project> getProjects() {
        return projectRepository.findAllByStartDateEndDate();
    }

}