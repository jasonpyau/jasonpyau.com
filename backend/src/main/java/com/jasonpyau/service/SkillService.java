package com.jasonpyau.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.SkillRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = CacheUtil.SKILL_CACHE, allEntries = true)
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
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String updateSkill(Skill updateSkill) {
        Optional<Skill> optional = skillRepository.findSkillByName(updateSkill.getName());
        if (!optional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        Skill skill = optional.get();
        Patch.merge(updateSkill, skill, "id", "name");
        Set<ConstraintViolation<Skill>> violations = validator.validate(skill);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        if (!skill.checkValidType()) {
            return Skill.SKILL_TYPE_ERROR;
        }
        skillRepository.save(skill);
        return null;
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String deleteSkill(String skillName) {
        Optional<Skill> optional = skillRepository.findSkillByName(skillName);
        if (!optional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        skillRepository.delete(optional.get());
        return null;
    }

    @Cacheable(cacheNames = CacheUtil.SKILL_CACHE)
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
