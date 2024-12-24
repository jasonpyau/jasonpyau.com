package com.jasonpyau.util;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {
    
    public static final String SKILL_CACHE = "skillCache";
    public static final String SKILL_ICON_SVG_CACHE = "skillIconSvgCache";
    public static final String PROJECT_CACHE = "projectCache";
    public static final String EXPERIENCE_CACHE = "experienceCache";
    public static final String ABOUT_ME_CACHE = "aboutMeCache";

    @Scheduled(fixedRate = 4, timeUnit = TimeUnit.HOURS)
    @CacheEvict(cacheNames = {SKILL_CACHE, SKILL_ICON_SVG_CACHE, PROJECT_CACHE, ABOUT_ME_CACHE, EXPERIENCE_CACHE}, allEntries = true)
    public void clearCache() {
        System.out.printf("%s: Cleared Cache\n", DateFormat.MMddyyyyhhmmss());
    }
}
