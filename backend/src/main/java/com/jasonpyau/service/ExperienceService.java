package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Experience;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ExperienceRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.DateFormat;

@Service
public class ExperienceService {
    
    @Autowired
    private ExperienceRepository experienceRepository;

    @Autowired
    private SkillService skillService;

    @CacheEvict(cacheNames = CacheUtil.EXPERIENCE_CACHE, allEntries = true)
    public void newExperience(Experience experience) {
        if (experience.getPresent()) {
            experience.syncEndDate();
        } else {
            experience.createOrder();
        }
        experienceRepository.save(experience);
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String newExperienceSkill(String skillName, Integer id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (!experienceOptional.isPresent()) {
            return Experience.EXPERIENCE_ID_ERROR;
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        Experience experience = experienceOptional.get();
        experience.addSkill(skillOptional.get());
        experienceRepository.save(experience);
        return null;
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String deleteExperienceSkill(String skillName, Integer id) {
        Optional<Experience> experienceOptional = experienceRepository.findById(id);
        if (!experienceOptional.isPresent()) {
            return Experience.EXPERIENCE_ID_ERROR;
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        Experience experience = experienceOptional.get();
        experience.deleteSkill(skillOptional.get());
        experienceRepository.save(experience);
        return null;
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
