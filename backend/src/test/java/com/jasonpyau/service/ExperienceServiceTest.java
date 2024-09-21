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


import com.jasonpyau.entity.Experience;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ExperienceRepository;

import jakarta.validation.ConstraintViolationException;

@ExtendWith(MockitoExtension.class)
public class ExperienceServiceTest {
    
    @Mock
    private ExperienceRepository experienceRepository;

    @Mock
    private SkillService skillService;

    @InjectMocks
    private ExperienceService experienceService;

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
                            .type("Language")
                            .simpleIconsIconSlug("spring")
                            .build();

    @Test
    public void testUpdateExperience() {
        given(experienceRepository.findById(1)).willReturn(Optional.of(experience));
        Experience updateExperience = new Experience();
        updateExperience.setPosition("Software Engineer");
        updateExperience.setStartDate("09/2024");
        updateExperience.setEndDate("09/2024");
        updateExperience.setPresent(true);
        String errorMessage = experienceService.updateExperience(updateExperience, 1);
        assertEquals(null, errorMessage);
    }

    @Test
    public void testUpdateExperience_ConstraintViolationException() {
        given(experienceRepository.findById(1)).willReturn(Optional.of(experience));
        Experience updateExperience = new Experience();
        updateExperience.setPosition("Software Engineer");
        updateExperience.setStartDate("09/2024");
        updateExperience.setEndDate("Not an end date");
        updateExperience.setPresent(true);
        assertThrows(ConstraintViolationException.class, () -> {
            experienceService.updateExperience(updateExperience, 1);
        });
    }

    @Test
    public void testNewExperienceSkill() {
        given(experienceRepository.findById(1)).willReturn(Optional.of(experience));
        given(skillService.getSkillByName("Java")).willReturn(Optional.of(skill));
        String errorMessage = experienceService.newExperienceSkill("Java", 1);
        assertEquals(null, errorMessage);
    }

    @Test
    public void testNewExperienceSkill_SkillNotFound_Error() {
        given(experienceRepository.findById(1)).willReturn(Optional.of(experience));
        String errorMessage = experienceService.newExperienceSkill("Java", 1);
        assertEquals(Skill.SKILL_NOT_FOUND_ERROR, errorMessage);
    }
}
