package com.jasonpyau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.exception.ResourceAlreadyExistsException;
import com.jasonpyau.repository.SkillRepository;

@ExtendWith(MockitoExtension.class)
public class SkillServiceTest {
    
    @Mock
    private SkillRepository skillRepository;

    @InjectMocks
    private SkillService skillService;

    private Skill java = Skill.builder()
                            .id(1)
                            .name("Java")
                            .type(Skill.Type.LANGUAGE)
                            .link("https://en.wikipedia.org/wiki/Java_(programming_language)")
                            .simpleIconsIconSlug("java")
                            .build();
    
    private Skill springBoot = Skill.builder()
                            .id(1)
                            .name("Spring Boot")
                            .type(Skill.Type.FRAMEWORK_OR_LIBRARY)
                            .link("https://en.wikipedia.org/wiki/Spring_Boot")
                            .simpleIconsIconSlug("springboot")
                            .build();                  

    @Test
    public void newSkill_SkillAlreadyExistsError() {
        given(skillRepository.findSkillByName("Java")).willReturn(Optional.of(java));
        ResourceAlreadyExistsException e = assertThrows(ResourceAlreadyExistsException.class, () -> {
            skillService.newSkill(java);
        });
        assertEquals(Skill.SKILL_ALREADY_EXISTS_ERROR, e.getMessage());
    }

    @Test
    public void getSkillIconSvgJava() {
        given(skillRepository.findSkillByName("Java")).willReturn(Optional.of(java));
        String svg = skillService.getSkillIconSvg("Java");
        assertNotEquals(svg, Skill.SKILL_ICON_EMPTY_SVG);
        assertTrue(svg.contains("fill"));
    }

    @Test
    public void getSkillIconSvgSpringBoot() {
        given(skillRepository.findSkillByName("Spring Boot")).willReturn(Optional.of(springBoot));
        String svg = skillService.getSkillIconSvg("Spring Boot");
        assertNotEquals(svg, Skill.SKILL_ICON_EMPTY_SVG);
        assertTrue(svg.contains("fill"));
    }
}
