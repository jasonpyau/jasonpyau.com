package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.AboutMe;
import com.jasonpyau.repository.AboutMeRepository;

@Service
public class AboutMeService {

    public static final String ABOUT_ME_TEXT_ERROR = "'text' should be between 1-1000 characters.";
    public static final String ABOUT_ME_LOAD_ERROR = "Error in loading About Me.";

    @Autowired
    private AboutMeRepository aboutMeRepository;
    
    // Returns error message if applicable, else null.
    public String setAboutMe(AboutMe aboutMe) {
        String text = aboutMe.getText();
        if (text == null || text.isBlank() || text.length() > 1000) {
            return ABOUT_ME_TEXT_ERROR;
        }
        aboutMeRepository.save(aboutMe);
        return null;
    }

    public String getAboutMe() {
        Optional<AboutMe> optional = aboutMeRepository.findById(1);
        if (!optional.isPresent()) {
            return ABOUT_ME_LOAD_ERROR;
        }
        return optional.get().getText();
    }
}
