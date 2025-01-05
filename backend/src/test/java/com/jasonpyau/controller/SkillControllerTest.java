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

import com.jasonpyau.entity.Skill;
import com.jasonpyau.entity.Skill.SkillType;
import com.jasonpyau.service.SkillService;

@WebMvcTest(SkillController.class)
public class SkillControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SkillService skillService;

    private Skill skill1 = Skill.builder()
                            .id(1)
                            .name("Java")
                            .type(SkillType.LANGUAGE)
                            .link("https://en.wikipedia.org/wiki/Java_(programming_language)")
                            .simpleIconsIconSlug("java")
                            .hexFill("#ffffff")
                            .build();

    private Skill skill2 = Skill.builder()
                            .id(2)
                            .name("Git")
                            .type(SkillType.SOFTWARE)
                            .link("https://en.wikipedia.org/wiki/Git")
                            .simpleIconsIconSlug("git")
                            .hexFill("#ffffff")
                            .build();

    @Test
    public void testGetSkills() throws Exception {
        HashMap<String, List<Skill>> skills = new HashMap<>();
        skills.put("Language", Arrays.asList(skill1));
        skills.put("Framework/Library", Arrays.asList());
        skills.put("Database", Arrays.asList());
        skills.put("Software", Arrays.asList(skill2));
        given(skillService.getSkills()).willReturn(skills);
        mockMvc.perform(MockMvcRequestBuilders.get("/skills/get")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.skills.Language", hasSize(1)))
            .andExpect(jsonPath("$.skills.Language[0].id", is(skill1.getId())))
            .andExpect(jsonPath("$.skills.Language[0].name", is(skill1.getName())))
            .andExpect(jsonPath("$.skills.Language[0].type", is(skill1.getType().getJsonValue())))
            .andExpect(jsonPath("$.skills.Language[0].link", is(skill1.getLink())))
            .andExpect(jsonPath("$.skills.Language[0].simpleIconsIconSlug", is(skill1.getSimpleIconsIconSlug())))
            .andExpect(jsonPath("$.skills.Language[0].hexFill", is(skill1.getHexFill())))
            .andExpect(jsonPath("$.skills.Framework/Library", hasSize(0)))
            .andExpect(jsonPath("$.skills.Database", hasSize(0)))
            .andExpect(jsonPath("$.skills.Software", hasSize(1)))
            .andExpect(jsonPath("$.skills.Software[0].id", is(skill2.getId())))
            .andExpect(jsonPath("$.skills.Software[0].name", is(skill2.getName())))
            .andExpect(jsonPath("$.skills.Software[0].type", is(skill2.getType().getJsonValue())))
            .andExpect(jsonPath("$.skills.Software[0].link", is(skill2.getLink())))
            .andExpect(jsonPath("$.skills.Software[0].simpleIconsIconSlug", is(skill2.getSimpleIconsIconSlug())))
            .andExpect(jsonPath("$.skills.Software[0].hexFill", is(skill2.getHexFill())))
            .andExpect(jsonPath("$.status", is("success")));
    }
    
}
