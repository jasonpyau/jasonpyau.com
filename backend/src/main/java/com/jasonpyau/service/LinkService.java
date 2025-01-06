package com.jasonpyau.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.jasonpyau.entity.Link;
import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.repository.LinkRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.DateFormat;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class LinkService {
    
    @Autowired
    private LinkRepository linkRepository;

    @Autowired
    private SimpleIconsService simpleIconsService;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @CacheEvict(cacheNames = CacheUtil.LINK_CACHE, allEntries = true)
    public void newLink(Link link) {
        link.setLastUpdatedUnixTime(DateFormat.getUnixTime());
        linkRepository.save(link);
    }

    @CacheEvict(cacheNames = CacheUtil.LINK_CACHE, allEntries = true)
    public void updateLink(Link updateLink, Integer id) {
        Optional<Link> optional = linkRepository.findById(id);
        if (!optional.isPresent()){
            throw new ResourceNotFoundException(Link.LINK_ID_ERROR);
        }
        Link link = optional.get();
        Patch.merge(updateLink, link, "id", "lastUpdatedUnixTime");
        Set<ConstraintViolation<Link>> violations = validator.validate(link);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        link.setLastUpdatedUnixTime(DateFormat.getUnixTime());
        linkRepository.save(link);
    }

    @CacheEvict(cacheNames = CacheUtil.LINK_CACHE, allEntries = true)
    public void moveLinkToTop(Integer id) {
        updateLink(new Link(), id);
    }

    @CacheEvict(cacheNames = CacheUtil.LINK_CACHE, allEntries = true)
    public void deleteLink(Integer id) {
        Optional<Link> optional = linkRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Link.LINK_ID_ERROR);
        }
        linkRepository.delete(optional.get());
    }

    @Cacheable(cacheNames = CacheUtil.LINK_CACHE)
    public List<Link> getLinks() {
        return linkRepository.findAllOrderedByLastUpdatedUnixTime();
    }

    @Cacheable(cacheNames = CacheUtil.LINK_CACHE)
    public String getLinkIconSvg(Integer id) {
        Optional<Link> optional = linkRepository.findById(id);
        if (!optional.isPresent()) {
            return SimpleIconsService.EMPTY_SVG;
        }
        Link link = optional.get();
        if (!StringUtils.hasText(link.getSimpleIconsIconSlug())) {
            return SimpleIconsService.EMPTY_SVG;
        }
        String svg = simpleIconsService.getSimpleIconsSvg(link.getSimpleIconsIconSlug());
        if (StringUtils.hasText(link.getHexFill()) && !svg.equals(SimpleIconsService.EMPTY_SVG)) {
            svg = simpleIconsService.replaceSvgFill(svg, link.getHexFill());
        }
        return svg;
    }
}
