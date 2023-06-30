package com.jasonpyau.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.SkillRepository;

@Service
public class SkillService {
    public static final String SKILL_NAME_ERROR = "'name' should be between 1-15 characters.";
    public static final String SKILL_ALREADY_EXISTS_ERROR = "Skill already exists.";
    public static final String SKILL_NOT_FOUND_ERROR = "Skill with given 'name' not found.";
    public static final String SKILL_TYPE_ERROR = "Invalid 'type'.";
    public static final HashSet<String> validTypes = new HashSet<>(Arrays.asList("Language", "Framework/Library", "Database", "Software"));

    @Autowired
    private SkillRepository skillRepository;

    // Returns error message if applicable, else null.
    public String newSkill(Skill skill) {
        String errorMessage = checkSkill(skill);
        if (errorMessage != null) {
            return errorMessage;
        }
        if (skillRepository.findSkillByName(skill.getName()).isPresent()) {
            return SKILL_ALREADY_EXISTS_ERROR;
        }
        skillRepository.save(skill);
        return null;
    }

    // Returns error message if applicable, else null.
    public String deleteSkill(String skillName) {
        String errorMessage = checkSkill(skillName);
        if (errorMessage != null) {
            return errorMessage;
        }
        Optional<Skill> optional = skillRepository.findSkillByName(skillName);
        if (!optional.isPresent()) {
            return SKILL_NOT_FOUND_ERROR;
        }
        skillRepository.delete(optional.get());
        return null;
    }

    public HashMap<String, List<String>> getSkills() {
        HashMap<String, List<String>> res = new HashMap<>();
        List<String> types = validTypes();
        for (String type : types) {
            res.put(type, skillRepository.findAllSkillNameByType(type));
        }
        return res;
    }

    public List<String> validTypes() {
        return new ArrayList<String>(validTypes);
    }

    private String checkSkill(Skill skill) {
        if (!validTypes.contains(skill.getType())) {
            return SKILL_TYPE_ERROR;
        }
        return checkSkill(skill.getName());
    }

    private String checkSkill(String skillName) {
        if (skillName == null || skillName.length() < 1 || skillName.length() > 15) {
            return SKILL_NAME_ERROR;
        }
        return null;
    }
}
