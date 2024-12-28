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
import com.jasonpyau.form.MetadataUpdateForm;
import com.jasonpyau.repository.MetadataRepository;
import com.jasonpyau.util.DateFormat;

@ExtendWith(MockitoExtension.class)
public class MetadataServiceTest {
    
    @Mock
    private MetadataRepository metadataRepository;

    @InjectMocks
    private MetadataService metadataService;

    private Metadata originalMetadata;
    private Metadata dummy;
    
    @BeforeEach
    public void setUp() {
        originalMetadata = Metadata.builder()
                                    .lastUpdated("06/25/2023")
                                    .views(999L)
                                    .name("Jason Yau")
                                    .iconLink("https://avatars.githubusercontent.com/u/113565962?v=4")
                                    .description("Jason Yau is a software engineer and a student studying Computer Science.")
                                    .keywords("software engineer, Computer Science, Java, developer")
                                    .build();
        dummy = Metadata.builder()
                        .lastUpdated(originalMetadata.getLastUpdated())
                        .views(originalMetadata.getViews())
                        .name(originalMetadata.getName())
                        .iconLink(originalMetadata.getIconLink())
                        .description(originalMetadata.getDescription())
                        .keywords(originalMetadata.getKeywords())
                        .build();
    }

    @Test
    public void testUpdateViews() {
        given(metadataRepository.findById(1)).willReturn(Optional.of(dummy));
        given(metadataRepository.save(dummy)).willReturn(dummy);
        Metadata metadata = metadataService.updateViews();
        assertEquals(originalMetadata.getId(), metadata.getId());
        assertEquals(originalMetadata.getViews()+1, metadata.getViews());
        assertEquals(originalMetadata.getLastUpdated(), metadata.getLastUpdated());
        assertEquals(originalMetadata.getName(), metadata.getName());
        assertEquals(originalMetadata.getIconLink(), metadata.getIconLink());
        assertEquals(originalMetadata.getDescription(), metadata.getDescription());
        assertEquals(originalMetadata.getKeywords(), metadata.getKeywords());
    }

    @Test
    public void testUpdateLastUpdated() {
        given(metadataRepository.findById(1)).willReturn(Optional.of(dummy));
        given(metadataRepository.save(dummy)).willReturn(dummy);
        Metadata metadata = metadataService.updateLastUpdated();
        assertEquals(originalMetadata.getId(), metadata.getId());
        assertEquals(originalMetadata.getViews(), metadata.getViews());
        assertEquals(DateFormat.MMddyyyy(), metadata.getLastUpdated());
        assertEquals(originalMetadata.getName(), metadata.getName());
        assertEquals(originalMetadata.getIconLink(), metadata.getIconLink());
        assertEquals(originalMetadata.getDescription(), metadata.getDescription());
        assertEquals(originalMetadata.getKeywords(), metadata.getKeywords());
    }

    @Test
    public void testUpdateWithForm() {
        given(metadataRepository.findById(1)).willReturn(Optional.of(dummy));
        given(metadataRepository.save(dummy)).willReturn(dummy);
        MetadataUpdateForm metadataUpdateForm = MetadataUpdateForm.builder()
                                                                    .name(originalMetadata.getName())
                                                                    .iconLink(originalMetadata.getIconLink())
                                                                    .description("I'm a Java software developer!")
                                                                    .keywords("Java, software developer")
                                                                    .build();
        Metadata metadata = metadataService.updateWithForm(metadataUpdateForm);
        assertEquals(originalMetadata.getId(), metadata.getId());
        assertEquals(originalMetadata.getViews(), metadata.getViews());
        assertEquals(originalMetadata.getLastUpdated(), metadata.getLastUpdated());
        assertEquals(metadataUpdateForm.getName(), metadata.getName());
        assertEquals(metadataUpdateForm.getIconLink(), metadata.getIconLink());
        assertEquals(metadataUpdateForm.getDescription(), metadata.getDescription());
        assertEquals(metadataUpdateForm.getKeywords(), metadata.getKeywords());
    }
}
