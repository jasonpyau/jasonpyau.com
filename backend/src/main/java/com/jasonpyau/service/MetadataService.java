package com.jasonpyau.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Metadata;
import com.jasonpyau.form.MetadataUpdateForm;
import com.jasonpyau.repository.MetadataRepository;
import com.jasonpyau.util.DateFormat;
import com.jasonpyau.util.Patch;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Validation;
import jakarta.validation.Validator;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;

    private Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    
    public Metadata getMetadata() {
        Optional<Metadata> optional = metadataRepository.findById(1);
        if (!optional.isPresent()) {
            Metadata metadata = Metadata.builder()
                                        .lastUpdated(DateFormat.MMddyyyy())
                                        .views(1L)
                                        .name("Jason Yau")
                                        .iconLink("https://avatars.githubusercontent.com/u/113565962?v=4")
                                        .description("Jason Yau is a software engineer and a student studying Computer Science.")
                                        .keywords("software engineer, Computer Science, Java, developer")
                                        .resumeLink("/files/Resume_Template.pdf")
                                        .build();
            metadataRepository.save(metadata);
            return metadata;
        }
        return optional.get();
    }

    public Metadata updateLastUpdated() {
        String date = DateFormat.MMddyyyy();
        Metadata metadata = getMetadata();
        metadata.setLastUpdated(date);
        return metadataRepository.save(metadata);
    }

    public Metadata updateViews() {
        Metadata metadata = getMetadata();
        metadata.setViews(metadata.getViews()+1);
        return metadataRepository.save(metadata);
    }

    public Metadata updateWithForm(MetadataUpdateForm metadataUpdateForm) {
        Metadata metadata = getMetadata();
        Patch.merge(metadataUpdateForm, metadata, "id", "lastUpdated", "views");
        Set<ConstraintViolation<Metadata>> violations = validator.validate(metadata);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return metadataRepository.save(metadata);
    }

}
