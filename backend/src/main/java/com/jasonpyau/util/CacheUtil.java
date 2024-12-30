package com.jasonpyau.util;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.jasonpyau.service.RateLimitService;

@Component
public class CacheUtil {
    
    public static final String SKILL_CACHE = "skillCache";
    public static final String SIMPLE_ICONS_SVG_CACHE = "simpleIconsSvgCache";
    public static final String PROJECT_CACHE = "projectCache";
    public static final String EXPERIENCE_CACHE = "experienceCache";
    public static final String ABOUT_ME_CACHE = "aboutMeCache";
    public static final String LINK_CACHE = "linkCache";

    @Autowired
    private RateLimitService rateLimitService;

    @Scheduled(fixedRateString = "${com.jasonpyau.cache.clear-rate:#{240}}", timeUnit = TimeUnit.MINUTES)
    @CacheEvict(cacheNames = {SKILL_CACHE, SIMPLE_ICONS_SVG_CACHE, PROJECT_CACHE, EXPERIENCE_CACHE, ABOUT_ME_CACHE, LINK_CACHE}, allEntries = true)
    public void clearCache() {
        System.out.printf("%s: Cleared skill, Simple Icons SVG, project, experience, about me, and link caches.\n", DateFormat.MMddyyyyhhmmss());
        System.out.printf("%s: Cleaned up unused rate limit bucket caches. New cache size: %d\n", DateFormat.MMddyyyyhhmmss(), rateLimitService.cleanCache());
    }
}
