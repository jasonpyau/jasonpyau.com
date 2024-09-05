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

import com.jasonpyau.entity.Project;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.repository.ProjectRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.DateFormat;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private SkillService skillService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @CacheEvict(cacheNames = CacheUtil.PROJECT_CACHE, allEntries = true)
    public void newProject(Project project) {
        if (project.getPresent()) {
            project.syncEndDate();
        } else {
            project.createOrder();
        }
        projectRepository.save(project);
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String updateProject(Project updateProject, Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        Project project = optional.get();
        Patch.merge(updateProject, project, "id", "dateOrder");
        Set<ConstraintViolation<Project>> violations = validator.validate(project);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        newProject(project);
        return null;
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String deleteProject(Integer id) {
        Optional<Project> optional = projectRepository.findById(id);
        if (!optional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        Project project = optional.get();
        Set<Skill> skills = project.getSkills();
        for (Skill skill : skills) {
            skill.getProjects().remove(project);
        }
        skills.clear();
        projectRepository.delete(project);
        return null;
    }

    @Cacheable(cacheNames = CacheUtil.PROJECT_CACHE)
    public List<Project> getProjects() {
        return projectRepository.findAllByStartDateEndDate();
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String newProjectSkill(String skillName, Integer id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (!projectOptional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        Project project = projectOptional.get();
        project.addSkill(skillOptional.get());
        projectRepository.save(project);
        return null;
    }

    // Returns error message if applicable, else null.
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public String deleteProjectSkill(String skillName, Integer id) {
        Optional<Project> projectOptional = projectRepository.findById(id);
        if (!projectOptional.isPresent()) {
            return Project.PROJECT_ID_ERROR;
        }
        Optional<Skill> skillOptional = skillService.getSkillByName(skillName);
        if (!skillOptional.isPresent()) {
            return Skill.SKILL_NOT_FOUND_ERROR;
        }
        Project project = projectOptional.get();
        project.deleteSkill(skillOptional.get());
        projectRepository.save(project);
        return null;
    }

    @Scheduled(fixedRate = 8, timeUnit = TimeUnit.HOURS)
    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.SKILL_CACHE}, allEntries = true)
    public void syncAllEndDates() {
        List<Project> projects = projectRepository.findAll();
        for (Project project : projects) {
            if (project.syncEndDate()) {
                System.out.printf("%s: Synced endDate for: '%s'\n", DateFormat.MMddyyyyhhmmss(), project.getName());
                projectRepository.save(project);
            }
        }
    }
    
}