package com.jasonpyau.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    public static final String EXPERIENCE_POSITION_ERROR = "'position' should be between 1-30 characters.";
    public static final String EXPERIENCE_COMPANY_ERROR = "'company' should be between 1-30 characters.";
    public static final String EXPERIENCE_LOCATION_ERROR = "'location' should be between 1-30 characters.";
    public static final String EXPERIENCE_START_DATE_ERROR = "'startDate' should be in format 'MM/YYYY'.";
    public static final String EXPERIENCE_END_DATE_ERROR = "'endDate' should be in format 'MM/YYYY'.";
    public static final String EXPERIENCE_PRESENT_ERROR = "'present' should be true or false.";
    public static final String EXPERIENCE_BODY_ERROR = "'body' should be between 1-1000 characters.";
    public static final String EXPERIENCE_LOGO_LINK_ERROR = "'logoLink' should be between 4-250 characters.";
    public static final String EXPERIENCE_COMPANY_LINK_ERROR = "'companyLink' should be between 0-250 characters.";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "position", nullable = false)
    @Size(min = 1, max = 30, message = EXPERIENCE_POSITION_ERROR)
    @NotBlank(message = EXPERIENCE_POSITION_ERROR)
    private String position;

    @Column(name = "company", nullable = false)
    @Size(min = 1, max = 30, message = EXPERIENCE_COMPANY_ERROR)
    @NotBlank(message = EXPERIENCE_COMPANY_ERROR)
    private String company;

    @Column(name = "location", nullable = false)
    @Size(min = 1, max = 30, message = EXPERIENCE_LOCATION_ERROR)
    @NotBlank(message = EXPERIENCE_LOCATION_ERROR)
    private String location;

    @Column(name = "start_date", nullable = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/20[0-9]{2}$", message = EXPERIENCE_START_DATE_ERROR)
    @NotBlank(message = EXPERIENCE_START_DATE_ERROR)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/20[0-9]{2}$", message = EXPERIENCE_END_DATE_ERROR)
    @NotBlank(message = EXPERIENCE_END_DATE_ERROR)
    private String endDate;

    @Column(name = "present", nullable = false)
    @NotNull(message = EXPERIENCE_PRESENT_ERROR)
    private Boolean present;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Column(name = "date_order", nullable = false)
    private String dateOrder;

    @Column(name = "body", nullable = false, columnDefinition = "varchar(1000)")
    @Size(min = 1, max = 1000, message = EXPERIENCE_BODY_ERROR)
    @NotBlank(message = EXPERIENCE_BODY_ERROR)
    private String body;

    @Column(name = "skills")
    @ManyToMany(mappedBy = "experiences", fetch = FetchType.LAZY)
    @OrderBy("name ASC")
    private final Set<Skill> skills = new HashSet<>();

    @Column(name = "logo_link", nullable = false)
    @Size(min = 4, max = 250, message = EXPERIENCE_LOGO_LINK_ERROR)
    @NotBlank(message = EXPERIENCE_LOGO_LINK_ERROR)
    private String logoLink;

    @Column(name = "company_link", nullable = true)
    @Size(max = 250, message = EXPERIENCE_COMPANY_LINK_ERROR)
    private String companyLink;
}
