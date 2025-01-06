package com.jasonpyau.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jasonpyau.entity.Skill;
import com.jasonpyau.entity.Skill.SkillType;
import com.jasonpyau.exception.ResourceAlreadyExistsException;
import com.jasonpyau.exception.ResourceNotFoundException;
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

    @Autowired
    private SimpleIconsService simpleIconsService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @CacheEvict(cacheNames = {CacheUtil.SKILL_CACHE}, allEntries = true)
    public void newSkill(Skill skill) {
        if (skillRepository.findByName(skill.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(Skill.SKILL_ALREADY_EXISTS_ERROR);
        }
        skillRepository.save(skill);
    }

    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void updateSkill(Skill updateSkill) {
        Optional<Skill> optional = skillRepository.findByName(updateSkill.getName());
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Skill.SKILL_NOT_FOUND_ERROR);
        }
        Skill skill = optional.get();
        Patch.merge(updateSkill, skill, "id", "name");
        Set<ConstraintViolation<Skill>> violations = validator.validate(skill);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        skillRepository.save(skill);
    }

    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void deleteSkill(String skillName) {
        Optional<Skill> optional = skillRepository.findByName(skillName);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Skill.SKILL_NOT_FOUND_ERROR);
        }
        skillRepository.delete(optional.get());
    }

    @Cacheable(cacheNames = CacheUtil.SKILL_CACHE)
    public HashMap<String, List<Skill>> getSkills() {
        HashMap<String, List<Skill>> res = new HashMap<>();
        for (SkillType type : SkillType.values()) {
            res.put(type.getJsonValue(), skillRepository.findAllByTypeNameOrderedByName(type.name()));
        }
        return res;
    }

    @Cacheable(cacheNames = CacheUtil.SKILL_CACHE)
    public String getSkillIconSvg(String skillName) {
        Optional<Skill> optional = skillRepository.findByName(skillName);
        if (!optional.isPresent()) {
            return SimpleIconsService.EMPTY_SVG;
        }
        Skill skill = optional.get();
        if (!StringUtils.hasText(skillName)) {
            return SimpleIconsService.EMPTY_SVG;
        }
        String svg = simpleIconsService.getSimpleIconsSvg(skill.getSimpleIconsIconSlug());
        if (StringUtils.hasText(skill.getHexFill()) && !svg.equals(SimpleIconsService.EMPTY_SVG)) {
            svg = simpleIconsService.replaceSvgFill(svg, skill.getHexFill());
        }
        return svg;
    }

    public List<String> validTypes() {
        return Skill.validTypes();
    }

    public Optional<Skill> getSkillByName(String skillName) {
        return skillRepository.findByName(skillName);
    }

}
