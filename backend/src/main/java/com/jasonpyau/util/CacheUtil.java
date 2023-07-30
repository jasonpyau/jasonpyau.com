package com.jasonpyau.util;

import java.util.concurrent.TimeUnit;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CacheUtil {
    
    public static final String SKILL_CACHE = "skillCache";
    public static final String PROJECT_CACHE = "projectCache";
    public static final String ABOUT_ME_CACHE = "aboutMeCache";

    @Scheduled(fixedRate = 4, timeUnit = TimeUnit.HOURS)
    @CacheEvict(cacheNames = {SKILL_CACHE, PROJECT_CACHE, ABOUT_ME_CACHE}, allEntries = true)
    public void clearCache() {
        System.out.println("Cleared Cache at "+DateFormat.MMddyyyyhhmmss());
    }
}
