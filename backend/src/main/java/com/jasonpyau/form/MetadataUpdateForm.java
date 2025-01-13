package com.jasonpyau.form;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MetadataUpdateForm {
    
    private String name;
    private String iconLink;
    private String description;
    private String keywords;
    private String resumeLink;
}
