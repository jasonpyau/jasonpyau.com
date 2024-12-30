package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Experience;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.repository.ExperienceRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.DateFormat;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class ExperienceService {
    
    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private SkillService skillService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @CacheEvict(cacheNames = CacheUtil.EXPERIENCE_CACHE, allEntries = true)
    public void newExperience(Experience experience) {
        if (experience.getPresent()) {
            experience.syncEndDate();
        } else {
            experience.createOrder();
        }
        experienceRepository.save(experience);
    }

    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void updateExperience(Experience updateExperience, Integer id) {
        Optional<Experience> optional = experienceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Experience.EXPERIENCE_ID_ERROR);
        }
        Experience experience = optional.get();
        Patch.merge(updateExperience, experience, "id", "dateOrder");
        Set<ConstraintViolation<Experience>> violations = validator.validate(experience);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        newExperience(experience);
    }

    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void deleteExperience(Integer id) {
        Optional<Experience> optional = experienceRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Experience.EXPERIENCE_ID_ERROR);
        }
        Experience experience = optional.get();
        Set<Skill> skills = experience.getSkills();
        for (Skill skill : skills) {
            skill.getExperiences().remove(experience);
        }
        skills.clear();
        experienceRepository.delete(experience);
    }

    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void newExperienceSkill(String skillName, Integer id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (!experienceOptional.isPresent()) {
            throw new ResourceNotFoundException(Experience.EXPERIENCE_ID_ERROR);
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            throw new ResourceNotFoundException(Skill.SKILL_NOT_FOUND_ERROR);
        }
        Experience experience = experienceOptional.get();
        experience.addSkill(skillOptional.get());
        experienceRepository.save(experience);
    }

    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void deleteExperienceSkill(String skillName, Integer id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (!experienceOptional.isPresent()) {
            throw new ResourceNotFoundException(Experience.EXPERIENCE_ID_ERROR);
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            throw new ResourceNotFoundException(Skill.SKILL_NOT_FOUND_ERROR);
        }
        Experience experience = experienceOptional.get();
        experience.deleteSkill(skillOptional.get());
        experienceRepository.save(experience);
    }

    @Cacheable(cacheNames = CacheUtil.EXPERIENCE_CACHE)
    public List<Experience> getExperiences() {
        return experienceRepository.findAllByDate();
    }

    @Scheduled(fixedRate = 8, timeUnit = TimeUnit.HOURS)
    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void syncAllEndDates() {
        List<Experience> experiences = experienceRepository.findAll();
        for (Experience experience : experiences) {
            if (experience.syncEndDate()) {
                System.out.printf("%s: Synced endDate for: '%s' at '%s'\n", DateFormat.MMddyyyyhhmmss(), experience.getPosition(), experience.getCompany());
                experienceRepository.save(experience);
            }
        }
    }
}
