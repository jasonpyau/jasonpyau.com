package com.jasonpyau.controller;

import static org.hamcrest.Matchers.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jasonpyau.service.SkillService;

@WebMvcTest(SkillController.class)
public class SkillControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    @Test
    public void testGetSkills() throws Exception {
        HashMap<String, List<String>> skills = new HashMap<>();
        skills.put("Language", Arrays.asList("React Native", "Java"));
        skills.put("Framework/Library", Arrays.asList());
        skills.put("Database", Arrays.asList());
        skills.put("Software", Arrays.asList("Git"));
        given(skillService.getSkills()).willReturn(skills);
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/get")
            .header("X-Forwarded-For", "localhost"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.skills.Language", hasSize(2)))
            .andExpect(jsonPath("$.skills.Language[0]", is("React Native")))
            .andExpect(jsonPath("$.skills.Language[1]", is("Java")))
            .andExpect(jsonPath("$.skills.Framework/Library", hasSize(0)))
            .andExpect(jsonPath("$.skills.Database", hasSize(0)))
            .andExpect(jsonPath("$.skills.Software", hasSize(1)))
            .andExpect(jsonPath("$.skills.Software[0]", is("Git")))
            .andExpect(jsonPath("$.status", is("success")));
    }
    
}
