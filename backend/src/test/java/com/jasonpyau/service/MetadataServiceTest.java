package com.jasonpyau.service;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jasonpyau.entity.Metadata;
import com.jasonpyau.repository.MetadataRepository;
import com.jasonpyau.util.DateFormat;

@ExtendWith(MockitoExtension.class)
public class MetadataServiceTest {
    
    @Mock
    private MetadataRepository metadataRepository;

    @InjectMocks
    private MetadataService metadataService;

    private Metadata originalMetadata;
    
    @BeforeEach
    public void setUp() {
        originalMetadata = Metadata.builder()
                                    .lastUpdated("06/25/2023")
                                    .views(999L)
                                    .name("Jason Yau")
                                    .build();
    }

    @Test
    void testUpdateViews() {
        Metadata dummy = Metadata.builder()
                                    .lastUpdated(originalMetadata.getLastUpdated())
                                    .views(originalMetadata.getViews())
                                    .name(originalMetadata.getName())
                                    .build();
        given(metadataRepository.findById(1)).willReturn(Optional.of(dummy));
        given(metadataRepository.save(dummy)).willReturn(dummy);
        Metadata metadata = metadataService.updateViews();
        assertEquals(originalMetadata.getId(), metadata.getId());
        assertEquals(originalMetadata.getViews()+1, metadata.getViews());
        assertEquals(originalMetadata.getLastUpdated(), metadata.getLastUpdated());
        assertEquals(originalMetadata.getName(), metadata.getName());
    }

    @Test
    void testUpdateLastUpdated() {
        Metadata dummy = Metadata.builder()
                                    .lastUpdated(originalMetadata.getLastUpdated())
                                    .views(originalMetadata.getViews())
                                    .name(originalMetadata.getName())
                                    .build();
        given(metadataRepository.findById(1)).willReturn(Optional.of(dummy));
        given(metadataRepository.save(dummy)).willReturn(dummy);
        Metadata metadata = metadataService.updateLastUpdated();
        assertEquals(originalMetadata.getId(), metadata.getId());
        assertEquals(originalMetadata.getViews(), metadata.getViews());
        assertEquals(DateFormat.MMddyyyy(), metadata.getLastUpdated());
        assertEquals(originalMetadata.getName(), metadata.getName());
    }
}
