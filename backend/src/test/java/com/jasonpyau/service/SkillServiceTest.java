package com.jasonpyau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    
    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill skill = new Skill(1, "Java", "Language");

    @Test
    private void newSkill_SkillAlreadyExistsError() {
        given(skillRepository.findSkillByName("Java")).willReturn(Optional.of(skill));
        String errorMessage = skillService.newSkill(skill);
        assertEquals(Skill.SKILL_ALREADY_EXISTS_ERROR, errorMessage);
    }
}
