package com.jasonpyau.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jasonpyau.entity.Project;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ProjectRepository;
import com.jasonpyau.service.ProjectService;


@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectRepository projectRepository;

    private Project project = Project.builder()
                                .id(1)
                                .name("Project1")
                                .description("Test Description of Project1")
                                .startDate("05/2023")
                                .endDate("06/2023")
                                .present(false)
                                .link("project.com")
                                .build();
    
    private Skill skill = Skill.builder()
                            .id(1)
                            .name("Java")
                            .type(Skill.Type.LANGUAGE)
                            .simpleIconsIconSlug("spring")
                            .hexFill("#ffffff")
                            .build();
    @BeforeEach
    public void setUp() {
        project.getSkills().add(skill);
    }

    @Test
    public void testGetProjects() throws Exception {
        List<Project> projects = new ArrayList<>(Arrays.asList(project));
        given(projectService.getProjects()).willReturn(projects);
        mockMvc.perform(MockMvcRequestBuilders.get("/projects/get")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.projects", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].id", is(project.getId())))
            .andExpect(jsonPath("$.projects[0].name", is(project.getName())))
            .andExpect(jsonPath("$.projects[0].description", is(project.getDescription())))
            .andExpect(jsonPath("$.projects[0].startDate", is(project.getStartDate())))
            .andExpect(jsonPath("$.projects[0].endDate", is(project.getEndDate())))
            .andExpect(jsonPath("$.projects[0].present", is(project.getPresent())))
            .andExpect(jsonPath("$.projects[0].skills", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].skills[0].id", is(skill.getId())))
            .andExpect(jsonPath("$.projects[0].skills[0].name", is(skill.getName())))
            .andExpect(jsonPath("$.projects[0].skills[0].type", is(skill.getType().getJsonValue())))
            .andExpect(jsonPath("$.projects[0].skills[0].simpleIconsIconSlug", is(skill.getSimpleIconsIconSlug())))
            .andExpect(jsonPath("$.projects[0].skills[0].hexFill", is(skill.getHexFill())))
            .andExpect(jsonPath("$.projects[0].link", is(project.getLink())));
    }
}
