package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "metadata")
public class Metadata {

    public static final String METADATA_NAME_ERROR = "'name' should be between 1-50 characters.";
    public static final String METADATA_ICON_LINK_ERROR = "'iconLink' should be between 4-500 characters.";

    @Id
    @Column(name = "id")
    private final Integer id = 1;

    @Column(name = "last_updated", nullable = false)
    private String lastUpdated;
    
    @Column(name = "views", nullable = false)
    private Long views;

    @Column(name = "name", nullable = false)
    @Size(min = 1, max = 50, message = METADATA_NAME_ERROR)
    @NotBlank(message = METADATA_NAME_ERROR)
    private String name;

    @Column(name = "icon_link", nullable = false)
    @Size(min = 4, max = 500, message = METADATA_ICON_LINK_ERROR)
    @NotBlank(message = METADATA_ICON_LINK_ERROR)
    private String iconLink;

}
