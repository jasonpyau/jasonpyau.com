package com.jasonpyau.service;

import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Metadata;
import com.jasonpyau.repository.MetadataRepository;
import com.jasonpyau.util.DateFormat;

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

    private void validateMetadata(Metadata metadata) {
        Set<ConstraintViolation<Metadata>> violations = validator.validate(metadata);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

    public Metadata updateName(String name) {
        Metadata metadata = getMetadata();
        metadata.setName(name);
        validateMetadata(metadata);
        return metadataRepository.save(metadata);
    }

    public Metadata updateIconLink(String iconLink) {
        Metadata metadata = getMetadata();
        metadata.setIconLink(iconLink);
        validateMetadata(metadata);
        return metadataRepository.save(metadata);
    }
}
