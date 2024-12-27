package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Metadata;
import com.jasonpyau.repository.MetadataRepository;
import com.jasonpyau.util.DateFormat;

@Service
public class MetadataService {

    @Autowired
    private MetadataRepository metadataRepository;
    
    public Metadata getMetadata() {
        Optional<Metadata> optional = metadataRepository.findById(1);
        if (!optional.isPresent()) {
            Metadata metadata = new Metadata(1, DateFormat.MMddyyyy(), 0L);
            metadataRepository.save(metadata);
            return metadata;
        }
        return optional.get();
    }

    public Metadata updateLastUpdated() {
        String date = DateFormat.MMddyyyy();
        Metadata metadata = getMetadata();
        metadata.setDate(date);
        return metadataRepository.save(metadata);
    }

    public Metadata updateViews() {
        Metadata metadata = getMetadata();
        metadata.setViews(metadata.getViews()+1);
        return metadataRepository.save(metadata);
    }
}
