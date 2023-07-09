package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "about_me")
public class AboutMe {

    public static final String ABOUT_ME_TEXT_ERROR = "'text' should be between 1-1000 characters.";
    public static final String ABOUT_ME_LOAD_ERROR = "Error in loading About Me.";

    @Id
    @Column(name = "id")
    private Integer id = 1;
    
    @Column(name = "text", columnDefinition = "varchar(1000)")
    @Size(max = 1000, message = ABOUT_ME_TEXT_ERROR)
    @NotBlank(message = ABOUT_ME_TEXT_ERROR)
    private String text;
    
}
