package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.AboutMe;
import com.jasonpyau.repository.AboutMeRepository;

@Service
public class AboutMeService {

    @Autowired
    private AboutMeRepository aboutMeRepository;
    
    public void setAboutMe(AboutMe aboutMe) {
        aboutMeRepository.save(aboutMe);
    }

    public String getAboutMe() {
        Optional<AboutMe> optional = aboutMeRepository.findById(1);
        if (!optional.isPresent()) {
            return AboutMe.ABOUT_ME_LOAD_ERROR;
        }
        return optional.get().getText();
    }
}
