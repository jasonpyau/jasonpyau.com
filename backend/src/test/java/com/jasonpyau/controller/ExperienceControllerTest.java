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

import com.jasonpyau.entity.Experience;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ExperienceRepository;
import com.jasonpyau.service.ExperienceService;

@WebMvcTest(ExperienceController.class)
public class ExperienceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExperienceService experienceService;

    @MockBean
    private ExperienceRepository experienceRepository;

    private Experience experience = Experience.builder()
                                        .id(1)
                                        .position("Software Engineer Intern")
                                        .company("Meta")
                                        .location("Menlo Park, CA")
                                        .startDate("05/2024")
                                        .endDate("08/2024")
                                        .present(false)
                                        .body("Software Engineer Intern working on engineering software at Meta as an Intern.")
                                        .logoLink("https://upload.wikimedia.org/wikipedia/commons/thumb/0/05/Meta_Platforms_Inc._logo_%28cropped%29.svg/75px-Meta_Platforms_Inc._logo_%28cropped%29.svg.png")
                                        .companyLink(null)
                                        .build();

    private Skill skill = Skill.builder()
                            .id(1)
                            .name("Java")
                            .type(Skill.Type.LANGUAGE)
                            .simpleIconsIconSlug("spring")
                            .build();
    
    @BeforeEach
    public void setUp() {
        experience.getSkills().add(skill);
    }
    
    @Test
    public void testGetExperiences() throws Exception {
        List<Experience> experiences = new ArrayList<>(Arrays.asList(experience));
        given(experienceService.getExperiences()).willReturn(experiences);
        mockMvc.perform(MockMvcRequestBuilders.get("/experiences/get")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.experiences", hasSize(1)))
            .andExpect(jsonPath("$.experiences[0].id", is(experience.getId())))
            .andExpect(jsonPath("$.experiences[0].position", is(experience.getPosition())))
            .andExpect(jsonPath("$.experiences[0].company", is(experience.getCompany())))
            .andExpect(jsonPath("$.experiences[0].location", is(experience.getLocation())))
            .andExpect(jsonPath("$.experiences[0].startDate", is(experience.getStartDate())))
            .andExpect(jsonPath("$.experiences[0].endDate", is(experience.getEndDate())))
            .andExpect(jsonPath("$.experiences[0].present", is(experience.getPresent())))
            .andExpect(jsonPath("$.experiences[0].body", is(experience.getBody())))
            .andExpect(jsonPath("$.experiences[0].skills", hasSize(1)))
            .andExpect(jsonPath("$.experiences[0].skills[0].id", is(skill.getId())))
            .andExpect(jsonPath("$.experiences[0].skills[0].name", is(skill.getName())))
            .andExpect(jsonPath("$.experiences[0].skills[0].type", is(skill.getType().getJsonValue())))
            .andExpect(jsonPath("$.experiences[0].skills[0].simpleIconsIconSlug", is(skill.getSimpleIconsIconSlug())))
            .andExpect(jsonPath("$.experiences[0].logoLink", is(experience.getLogoLink())))
            .andExpect(jsonPath("$.experiences[0].companyLink", is(experience.getCompanyLink())));
    }
}
