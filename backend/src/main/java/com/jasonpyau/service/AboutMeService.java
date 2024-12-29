package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.AboutMe;
import com.jasonpyau.repository.AboutMeRepository;
import com.jasonpyau.util.CacheUtil;

@Service
public class AboutMeService {

    @Autowired
    private AboutMeRepository aboutMeRepository;
    
    @CacheEvict(cacheNames = CacheUtil.ABOUT_ME_CACHE, allEntries = true)
    public void setAboutMe(AboutMe aboutMe) {
        aboutMeRepository.save(aboutMe);
    }

    @Cacheable(cacheNames = CacheUtil.ABOUT_ME_CACHE)
    public String getAboutMe() {
        Optional<AboutMe> optional = aboutMeRepository.findById(1);
        if (!optional.isPresent()) {
            AboutMe aboutMe = new AboutMe("This is the default About Me.");
            setAboutMe(aboutMe);
            return aboutMe.getText();
        }
        return optional.get().getText();
    }
}
