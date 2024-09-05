package com.jasonpyau.service;

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
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ProjectRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class ProjectServiceTest {
    
    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private ProjectService projectService;

    private Project project = Project.builder()
                                .id(1)
                                .name("Project1")
                                .description("Test Description of Project1")
                                .startDate("05/2023")
                                .endDate("06/2023")
                                .present(false)
                                .dateOrder("202305202306")
                                .link("project.com")
                                .build();
    
    private Skill skill = Skill.builder()
                            .id(1)
                            .name("Java")
                            .type("Language")
                            .simpleIconsIconSlug("spring")
                            .build();

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
        updateProject.setName("a".repeat(100));
        assertThrows(ConstraintViolationException.class, () -> {
            projectService.updateProject(updateProject, 1);
        });
    }

    @Test
    public void testUpdateProject_ProjectIdError() {
        String errorMessage = projectService.updateProject(project, 2);
        assertEquals(Project.PROJECT_ID_ERROR, errorMessage);
    }

    @Test
    public void testNewProjectSkill() {
        given(projectRepository.findById(1)).willReturn(Optional.of(project));
        given(skillService.getSkillByName("Java")).willReturn(Optional.of(skill));
        String errorMessage = projectService.newProjectSkill("Java", 1);
        assertEquals(null, errorMessage);
    }

    @Test
    public void testNewProjectSkill_SkillNotFound_Error() {
        given(projectRepository.findById(1)).willReturn(Optional.of(project));
        String errorMessage = projectService.newProjectSkill("Java", 1);
        assertEquals(Skill.SKILL_NOT_FOUND_ERROR, errorMessage);
    }
}
