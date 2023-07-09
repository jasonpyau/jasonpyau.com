package com.jasonpyau.entity;

import java.util.Arrays;
import java.util.HashSet;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
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
@Table(name = "skills", indexes = @Index(name = "type_name_ind", columnList = "type, name"))
public class Skill {

    public static final String SKILL_NAME_ERROR = "'name' should be between 1-15 characters.";
    public static final String SKILL_ALREADY_EXISTS_ERROR = "Skill already exists.";
    public static final String SKILL_NOT_FOUND_ERROR = "Skill with given 'name' not found.";
    public static final String SKILL_TYPE_ERROR = "Invalid 'type'.";
    public static final HashSet<String> validTypes = new HashSet<>(Arrays.asList("Language", "Framework/Library", "Database", "Software"));

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 1, max = 15, message = SKILL_NAME_ERROR)
    @NotBlank(message = SKILL_NAME_ERROR)
    String name;

    @Column(name = "type", nullable = false)
    @NotBlank(message = SKILL_TYPE_ERROR)
    String type;

    public boolean checkValidType() {
        return validTypes.contains(type);
    }

}
