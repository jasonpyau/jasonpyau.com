package com.jasonpyau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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

    @Test void getSkills() {
        given(skillRepository.findAllSkillsByTypeName(Skill.Type.LANGUAGE.name())).willReturn(List.of(java));
        given(skillRepository.findAllSkillsByTypeName(Skill.Type.FRAMEWORK_OR_LIBRARY.name())).willReturn(List.of(springBoot));
        assertDoesNotThrow(() -> {
            HashMap<String, List<Skill>> skills = skillService.getSkills();
            assertNotEquals(skills, null);
            assertNotEquals(skills.get(Skill.Type.LANGUAGE.getJsonValue()), null);
            assertEquals(skills.get(Skill.Type.LANGUAGE.getJsonValue()).size(), 1);
            assertEquals(skills.get(Skill.Type.LANGUAGE.getJsonValue()).get(0), java);
            assertNotEquals(skills.get(Skill.Type.FRAMEWORK_OR_LIBRARY.getJsonValue()), null);
            assertEquals(skills.get(Skill.Type.FRAMEWORK_OR_LIBRARY.getJsonValue()).size(), 1);
            assertEquals(skills.get(Skill.Type.FRAMEWORK_OR_LIBRARY.getJsonValue()).get(0), springBoot);
            assertNotEquals(skills.get(Skill.Type.DATABASE.getJsonValue()), null);
            assertEquals(skills.get(Skill.Type.DATABASE.getJsonValue()).size(), 0);
            assertNotEquals(skills.get(Skill.Type.SOFTWARE.getJsonValue()), null);
            assertEquals(skills.get(Skill.Type.SOFTWARE.getJsonValue()).size(), 0);
        });
    }
}
