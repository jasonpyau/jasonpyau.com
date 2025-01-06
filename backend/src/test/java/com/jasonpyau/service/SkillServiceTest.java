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
import com.jasonpyau.entity.Skill.SkillType;
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
                            .type(SkillType.LANGUAGE)
                            .link("https://en.wikipedia.org/wiki/Java_(programming_language)")
                            .simpleIconsIconSlug("java")
                            .hexFill("#ffffff")
                            .build();
    
    private Skill springBoot = Skill.builder()
                            .id(1)
                            .name("Spring Boot")
                            .type(SkillType.FRAMEWORK_OR_LIBRARY)
                            .link("https://en.wikipedia.org/wiki/Spring_Boot")
                            .simpleIconsIconSlug("springboot")
                            .hexFill("#ffffff")
                            .build();                  

    @Test
    public void newSkill_SkillAlreadyExistsError() {
        given(skillRepository.findByName("Java")).willReturn(Optional.of(java));
        ResourceAlreadyExistsException e = assertThrows(ResourceAlreadyExistsException.class, () -> {
            skillService.newSkill(java);
        });
        assertEquals(Skill.SKILL_ALREADY_EXISTS_ERROR, e.getMessage());
    }

    @Test void getSkills() {
        given(skillRepository.findAllByTypeNameOrderedByName(SkillType.LANGUAGE.name())).willReturn(List.of(java));
        given(skillRepository.findAllByTypeNameOrderedByName(SkillType.FRAMEWORK_OR_LIBRARY.name())).willReturn(List.of(springBoot));
        assertDoesNotThrow(() -> {
            HashMap<String, List<Skill>> skills = skillService.getSkills();
            assertNotEquals(skills, null);
            assertNotEquals(skills.get(SkillType.LANGUAGE.getJsonValue()), null);
            assertEquals(skills.get(SkillType.LANGUAGE.getJsonValue()).size(), 1);
            assertEquals(skills.get(SkillType.LANGUAGE.getJsonValue()).get(0), java);
            assertNotEquals(skills.get(SkillType.FRAMEWORK_OR_LIBRARY.getJsonValue()), null);
            assertEquals(skills.get(SkillType.FRAMEWORK_OR_LIBRARY.getJsonValue()).size(), 1);
            assertEquals(skills.get(SkillType.FRAMEWORK_OR_LIBRARY.getJsonValue()).get(0), springBoot);
            assertNotEquals(skills.get(SkillType.DATABASE.getJsonValue()), null);
            assertEquals(skills.get(SkillType.DATABASE.getJsonValue()).size(), 0);
            assertNotEquals(skills.get(SkillType.SOFTWARE.getJsonValue()), null);
            assertEquals(skills.get(SkillType.SOFTWARE.getJsonValue()).size(), 0);
        });
    }
}
