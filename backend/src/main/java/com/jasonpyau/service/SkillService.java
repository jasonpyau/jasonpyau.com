package com.jasonpyau.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.SkillRepository;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    // Returns error message if applicable, else null.
    public String newSkill(Skill skill) {
        if (skillRepository.findSkillByName(skill.getName()).isPresent()) {
            return Skill.SKILL_ALREADY_EXISTS_ERROR;
        }
        if (!skill.checkValidType()) {
            return Skill.SKILL_TYPE_ERROR;
        }
        skillRepository.save(skill);
        return null;
    }

    // Returns error message if applicable, else null.
    public String deleteSkill(String skillName) {
        Optional<Skill> optional = skillRepository.findSkillByName(skillName);
        if (!optional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        skillRepository.delete(optional.get());
        return null;
    }

    public HashMap<String, List<Skill>> getSkills() {
        HashMap<String, List<Skill>> res = new HashMap<>();
        List<String> types = validTypes();
        for (String type : types) {
            res.put(type, skillRepository.findAllSkillNameByType(type));
        }
        return res;
    }

    public List<String> validTypes() {
        return new ArrayList<String>(Skill.validTypes);
    }

    public Optional<Skill> getSkillByName(String skillName) {
        return skillRepository.findSkillByName(skillName);
    }

}
