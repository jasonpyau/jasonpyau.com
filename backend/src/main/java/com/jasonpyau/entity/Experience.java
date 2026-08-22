package com.jasonpyau.entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.jasonpyau.util.DateFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="experiences", indexes = @Index(name = "date_order_ind", columnList = "date_order"))
public class Experience {

    @Getter
    @Setter
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @EqualsAndHashCode(onlyExplicitlyIncluded = true)
    public static class Position implements Comparable<Position> {
        public static final String EXPERIENCE_POSITION_NOT_FOUND_ERROR = "Position with the same 'positionName' does not exist.";
        public static final String EXPERIENCE_POSITION_ALREADY_EXISTS_ERROR = "Position with the same 'positionName' already exists.";
        public static final String EXPERIENCE_POSITION_NAME_ERROR = "'positionName' should be between 1-50 characters.";
        public static final String EXPERIENCE_POSITION_START_DATE_ERROR = "'startDate' should be in format 'MM/YYYY'.";
        public static final String EXPERIENCE_POSITION_END_DATE_ERROR = "'endDate' should be in format 'MM/YYYY'.";
        public static final String EXPERIENCE_POSITION_PRESENT_ERROR = "'present' should be true or false.";
        
        @EqualsAndHashCode.Include
        @Size(min = 1, max = 50, message = EXPERIENCE_POSITION_NAME_ERROR)
        @NotBlank(message = EXPERIENCE_POSITION_NAME_ERROR)
        private String positionName;
        
        @Pattern(regexp = "^(0[1-9]|1[0-2])/[1-2]{1}[0-9]{3}$", message = EXPERIENCE_POSITION_START_DATE_ERROR)
        @NotBlank(message = EXPERIENCE_POSITION_START_DATE_ERROR)
        private String startDate;
        
        @Pattern(regexp = "^(0[1-9]|1[0-2])/[1-2]{1}[0-9]{3}$", message = EXPERIENCE_POSITION_END_DATE_ERROR)
        @NotBlank(message = EXPERIENCE_POSITION_END_DATE_ERROR)
        private String endDate;
        
        @NotNull(message = EXPERIENCE_POSITION_PRESENT_ERROR)
        private Boolean present;

        public static String getSortKey(String startDate, String endDate, Boolean present) {
            if (startDate == null || endDate == null || present == null) {
                return "0".repeat(13);
            }
            String[] startSplit = startDate.split("/", 2);
            String[] endSplit = endDate.split("/", 2);
            // EndYYYY+EndMM+present+StartYYYY+StartMM
            return endSplit[1]
                + endSplit[0]
                + (present ? "1" : "0")
                + startSplit[1]
                + startSplit[0];
        }
        
        @JsonIgnore
        public String getSortKey() {
            return getSortKey(startDate, endDate, present);
        }

        @Override
        public int compareTo(Position other) {
            return other.getSortKey().compareTo(this.getSortKey());
        }
    }

    public enum ExperienceType {
        WORK_EXPERIENCE,
        EDUCATION;
    }

    public static final String EXPERIENCE_ID_ERROR = "Invalid 'id', experience not found.";
    public static final String EXPERIENCE_ORGANIZATION_ERROR = "'organization' should be between 1-50 characters.";
    public static final String EXPERIENCE_LOCATION_ERROR = "'location' should be between 1-30 characters.";
    public static final String EXPERIENCE_BODY_ERROR = "'body' should be between 1-2000 characters.";
    public static final String EXPERIENCE_LOGO_LINK_ERROR = "'logoLink' should be between 2-500 characters and start with 'http://' or 'https://' or '/'.";
    public static final String EXPERIENCE_ORGANIZATION_LINK_ERROR = "'organizationLink' should be between 0-250 characters and if not empty, start with 'http://' or 'https://'.";
    public static final String EXPERIENCE_TYPE_ERROR = "'type' should be one of the following: "+validTypes()
                                                                                                .stream()
                                                                                                .map(type -> String.format("'%s'", type))
                                                                                                .toList()
                                                                                                .toString()+".";
    public static final String EXPERIENCE_TYPE_NULL_ERROR = "'type' should not be null.";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = EXPERIENCE_TYPE_NULL_ERROR)
    private ExperienceType type;

    @Column(name = "organization", nullable = false)
    @Size(min = 1, max = 50, message = EXPERIENCE_ORGANIZATION_ERROR)
    @NotBlank(message = EXPERIENCE_ORGANIZATION_ERROR)
    private String organization;

    @Column(name = "location", nullable = false)
    @Size(min = 1, max = 30, message = EXPERIENCE_LOCATION_ERROR)
    @NotBlank(message = EXPERIENCE_LOCATION_ERROR)
    private String location;

    @Column(name = "positions", nullable = false)
    @JdbcTypeCode(SqlTypes.JSON)
    @JsonIgnore
    private final Set<Position> positions = new HashSet<>();

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "date_order", nullable = false)
    private String dateOrder;

    @Column(name = "body", nullable = false, columnDefinition = "varchar(2000)")
    @Size(min = 1, max = 2000, message = EXPERIENCE_BODY_ERROR)
    @NotBlank(message = EXPERIENCE_BODY_ERROR)
    private String body;

    @Column(name = "skills")
    @ManyToMany(mappedBy = "experiences", fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    private final Set<Skill> skills = new HashSet<>();

    @Column(name = "logo_link", nullable = false)
    @Size(min = 2, max = 500, message = EXPERIENCE_LOGO_LINK_ERROR)
    @Pattern(regexp = "^((http|https):\\/\\/|\\/)(.*)$", message = EXPERIENCE_LOGO_LINK_ERROR)
    @NotBlank(message = EXPERIENCE_LOGO_LINK_ERROR)
    private String logoLink;

    @Column(name = "organization_link", nullable = true)
    @Size(max = 250, message = EXPERIENCE_ORGANIZATION_LINK_ERROR)
    @Pattern(regexp = "^([\\s]*|(http|https):\\/\\/(.*))$", message = EXPERIENCE_ORGANIZATION_LINK_ERROR)
    private String organizationLink;

    public void createOrder() {
        if (positions.isEmpty()) {
            this.dateOrder = Position.getSortKey(null, null, null);
        } else {
            String firstStartDate = Collections.min(positions.stream().map(Position::getStartDate).toList());
            String lastEndDate = Collections.max(positions.stream().map(Position::getEndDate).toList());
            boolean hasPresent = positions.stream().anyMatch(Position::getPresent);
            this.dateOrder = Position.getSortKey(firstStartDate, lastEndDate, hasPresent);
        }
    }

    // Returns true if there was a change to an endDate.
    public boolean syncEndDate() {
        boolean changed = false;
        for (Position position : positions) {
            if (position.present) {
                changed = !position.endDate.equals(DateFormat.MMyyyy());
                position.endDate = DateFormat.MMyyyy();
            }
        }
        if (changed) {
            createOrder();
        }
        return changed;
    }

    public void addSkill(Skill skill) {
        skill.getExperiences().add(this);
        this.skills.add(skill);
    }

    public void deleteSkill(Skill skill) {
        skill.getExperiences().remove(this);
        this.skills.remove(skill);
    }

    public static List<String> validTypes() {
        return Arrays.stream(ExperienceType.values()).map(ExperienceType::name).toList();
    }

    @JsonProperty("positions")
    public List<Position> sortedPositions() {
        return positions.stream().sorted().toList();
    }

    public Optional<Position> getPosition(String positionName) {
        return positions.stream().filter(position -> position.getPositionName().equals(positionName)).findFirst();
    }
}
