package com.jasonpyau.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonValue;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
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
@Table(name = "skills", indexes = @Index(name = "type_name_ind", columnList = "type, name"))
public class Skill {

    public enum Type {
        LANGUAGE("Language"),
        FRAMEWORK_OR_LIBRARY("Framework/Library"),
        DATABASE("Database"),
        SOFTWARE("Software");

        @Getter
        @JsonValue
        private final String jsonValue;

        Type(String jsonValue) {
            this.jsonValue = jsonValue;
        }
    }

    public static final String SKILL_NAME_ERROR = "'name' should be between 1-25 characters.";
    public static final String SKILL_ALREADY_EXISTS_ERROR = "Skill already exists.";
    public static final String SKILL_NOT_FOUND_ERROR = "Skill with given 'name' not found.";
    public static final String SKILL_SIMPLE_ICONS_ICON_SLUG_ERROR = "'simpleIconsIconSlug' should be between 0-50 characters.";
    public static final String SKILL_LINK_ERROR = "'link' should be between 0-250 characters and if not empty, start with 'http://' or 'https://'.";
    public static final String SKILL_TYPE_ERROR = "'type' should be one of the following: "+validTypes().toString();
    public static final String SKILL_ICON_EMPTY_SVG = "";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", unique = true, nullable = false)
    @Size(min = 1, max = 25, message = SKILL_NAME_ERROR)
    @NotBlank(message = SKILL_NAME_ERROR)
    private String name;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull
    private Type type;

    @Column(name = "link", nullable = true)
    @Size(max = 250, message = SKILL_LINK_ERROR)
    @Pattern(regexp = "^([\\s]*|(http|https):\\/\\/(.*))$", message = SKILL_LINK_ERROR)
    private String link;

    @Column(name = "simple_icons_icon_slug", nullable = true)
    @Size(max = 50, message = SKILL_SIMPLE_ICONS_ICON_SLUG_ERROR)
    // https://www.npmjs.com/package/simple-icons
    // https://github.com/simple-icons/simple-icons/blob/master/slugs.md
    // https://github.com/simple-icons/simple-icons/blob/6.23.0/slugs.md
    private String simpleIconsIconSlug;

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Column(name = "projects")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "skill_project",
                joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "project_id", referencedColumnName = "id"))
    private final Set<Project> projects = new HashSet<>();

    @JsonIgnore
    @Setter(AccessLevel.NONE)
    @Column(name = "experiences")
    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "skill_experience",
                joinColumns = @JoinColumn(name = "skill_id", referencedColumnName = "id"),
                inverseJoinColumns = @JoinColumn(name = "experience_id", referencedColumnName = "id"))
    private final Set<Experience> experiences = new HashSet<>();

    public static List<String> validTypes() {
        ArrayList<String> validTypes = new ArrayList<>();
        for (Type type : Type.values()) {
            validTypes.add(type.getJsonValue());
        }
        return validTypes;
    }
}
