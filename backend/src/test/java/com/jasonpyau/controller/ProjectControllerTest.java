package com.jasonpyau.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jasonpyau.entity.Project;
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

    private Project project = new Project(1, "Project1", "Test Description of Project1", "05/2023", "06/2023", 
                                        "202305202306", new ArrayList<>(Arrays.asList("Java")), "project1.com");

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
            .andExpect(jsonPath("$.projects[0].dateOrder", is(project.getDateOrder())))
            .andExpect(jsonPath("$.projects[0].technologies", hasSize(1)))
            .andExpect(jsonPath("$.projects[0].technologies[0]", is(project.getTechnologies().get(0))))
            .andExpect(jsonPath("$.projects[0].link", is(project.getLink())));
    }
}
