package com.jasonpyau.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jasonpyau.entity.Project;
import com.jasonpyau.repository.ProjectRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    
    @Mock
    private ProjectRepository projectRepository;

    @InjectMocks
    private ProjectService projectService;

    private Project project = new Project(1, "Project1", "Test Description of Project1", "05/2023", "06/2023", 
                                        "202305202306", new ArrayList<>(Arrays.asList("Java")), "project1.com");

    @Test
    public void testUpdateProject() {
        given(projectRepository.findById(1)).willReturn(Optional.of(project));
        Project updateProject = new Project();
        updateProject.setName("Project2");
        String errorMessage = projectService.updateProject(updateProject, 1);
        assertEquals(null, errorMessage);
    }

    @Test
    public void testUpdateProject_ConstraintViolationException() {
        given(projectRepository.findById(1)).willReturn(Optional.of(project));
        Project updateProject = new Project();
        updateProject.setTechnologies(Arrays.asList("Java", "Spring Boot", null));
        assertThrows(ConstraintViolationException.class, () -> {
            projectService.updateProject(updateProject, 1);
        });
    }

    @Test
    public void testUpdateProject_ProjectIdError() {
        String errorMessage = projectService.updateProject(project, 2);
        assertEquals(Project.PROJECT_ID_ERROR, errorMessage);
    }
}
