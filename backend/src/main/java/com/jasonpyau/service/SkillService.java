package com.jasonpyau.service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.jasonpyau.entity.Skill;
import com.jasonpyau.exception.ResourceAlreadyExistsException;
import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.repository.SkillRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.Patch;
import com.jasonpyau.util.SkillIconSvgData;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class SkillService {

    @Autowired
    private SkillRepository skillRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    private JsonNode iconSvgData = SkillIconSvgData.get();

    @CacheEvict(cacheNames = {CacheUtil.SKILL_CACHE, CacheUtil.SKILL_ICON_SVG_CACHE}, allEntries = true)
    public void newSkill(Skill skill) {
        if (skillRepository.findSkillByName(skill.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException(Skill.SKILL_ALREADY_EXISTS_ERROR);
        }
        skillRepository.save(skill);
    }

    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE, CacheUtil.SKILL_ICON_SVG_CACHE}, allEntries = true)
    public void updateSkill(Skill updateSkill) {
        Optional<Skill> optional = skillRepository.findSkillByName(updateSkill.getName());
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

    @CacheEvict(cacheNames = {CacheUtil.PROJECT_CACHE, CacheUtil.EXPERIENCE_CACHE, CacheUtil.SKILL_CACHE, CacheUtil.SKILL_ICON_SVG_CACHE}, allEntries = true)
    public void deleteSkill(String skillName) {
        Optional<Skill> optional = skillRepository.findSkillByName(skillName);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Skill.SKILL_NOT_FOUND_ERROR);
        }
        skillRepository.delete(optional.get());
    }

    @Cacheable(cacheNames = CacheUtil.SKILL_CACHE)
    public HashMap<String, List<Skill>> getSkills() {
        HashMap<String, List<Skill>> res = new HashMap<>();
        for (Skill.Type type : Skill.Type.values()) {
            res.put(type.getJsonValue(), skillRepository.findAllSkillsByTypeName(type.name()));
        }
        return res;
    }

    @Cacheable(cacheNames = CacheUtil.SKILL_ICON_SVG_CACHE)
    public String getSkillIconSvg(String skillName) {
        Optional<Skill> optional = skillRepository.findSkillByName(skillName);
        if (!optional.isPresent() || optional.get().getSimpleIconsIconSlug().isBlank()) {
            return Skill.SKILL_ICON_EMPTY_SVG;
        }
        Skill skill = optional.get();
        RestClient restClient = RestClient.create();
        try {
            ResponseEntity<String> res = restClient
                                        .get()
                                        .uri("https://cdn.simpleicons.org/"+skill.getSimpleIconsIconSlug())
                                        .retrieve()
                                        .toEntity(String.class);
            return res.getBody();
        } catch (HttpClientErrorException e) {}
        // If we got here, we used simple-icons/6.23.0.
        try {
            ResponseEntity<String> res = restClient
                                        .get()
                                        .uri("https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/icons/"+skill.getSimpleIconsIconSlug()+".svg")
                                        .retrieve()
                                        .toEntity(String.class);
            String svg = res.getBody();
            Matcher matcher = Pattern.compile("<title>(.*?)</title>").matcher(svg);
            if (matcher.find() && iconSvgData != null) {
                String hex = null;
                String title = matcher.group(1);
                for (JsonNode data : iconSvgData) {
                    if (data.get("title").asText().equals(title)) {
                        hex = data.get("hex").asText();
                        break;
                    }
                }
                if (hex != null) {
                    svg = svg.replace("<svg", String.format("<svg fill=\"%s\"", "#"+hex));
                }
            }
            return svg;
        } catch (HttpClientErrorException e) {}
        return Skill.SKILL_ICON_EMPTY_SVG;
    }

    public List<String> validTypes() {
        return Skill.validTypes();
    }

    public Optional<Skill> getSkillByName(String skillName) {
        return skillRepository.findSkillByName(skillName);
    }

}
