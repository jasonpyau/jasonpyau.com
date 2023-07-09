package com.jasonpyau.entity;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Table(name="projects")
public class Project {
    
    public static final String PROJECT_ID_ERROR = "Invalid 'id', project not found.";
    public static final String PROJECT_NAME_ERROR = "'name' should be between 3-30 characters.";
    public static final String PROJECT_DESCRIPTION_ERROR = "'description' should be between 10-250 characters.";
    public static final String PROJECT_START_DATE_ERROR = "'startDate' should be in format 'MM/YYYY'.";
    public static final String PROJECT_END_DATE_ERROR = "'endDate' should be in format 'MM/YYYY'.";
    public static final String PROJECT_TECHNOLOGIES_ERROR = "'technologies' should have length between 1-10 and each technology between 1-15 characters.";
    public static final String PROJECT_LINK_ERROR = "'link' should be between 4-250 characters.";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;

    @Column(name = "name", nullable = false)
    @Size(min = 3, max = 30, message = PROJECT_NAME_ERROR)
    @NotBlank(message = PROJECT_NAME_ERROR)
    private String name;

    @Column(name = "description", nullable = false)
    @Size(min = 10, max = 250, message = PROJECT_DESCRIPTION_ERROR)
    @NotBlank(message = PROJECT_DESCRIPTION_ERROR)
    private String description;

    @Column(name = "start_date", nullable = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/20[0-9]{2}$", message = PROJECT_START_DATE_ERROR)
    @NotBlank(message = PROJECT_START_DATE_ERROR)
    private String startDate;

    @Column(name = "end_date", nullable = false)
    @Pattern(regexp = "^(0[1-9]|1[0-2])/20[0-9]{2}$", message = PROJECT_END_DATE_ERROR)
    @NotBlank(message = PROJECT_END_DATE_ERROR)
    private String endDate;

    @Column(name = "date_order", nullable = false)
    private String dateOrder;

    @Column(name = "technologies", nullable = false)
    @Size(min = 1, max = 10, message = PROJECT_TECHNOLOGIES_ERROR)
    private List<@NotBlank(message = PROJECT_TECHNOLOGIES_ERROR) @Size(min = 1, max = 15, message = PROJECT_TECHNOLOGIES_ERROR) String> technologies;

    @Column(name = "link", nullable = false)
    @Size(min = 4, max = 250, message = PROJECT_LINK_ERROR)
    @NotBlank(message = PROJECT_LINK_ERROR)
    private String link;

    public void createOrder() {
        StringBuilder sb = new StringBuilder(14);
        sb.append(startDate.substring(3));
        sb.append(startDate.substring(0, 2));
        sb.append(endDate.substring(3));
        sb.append(endDate.substring(0, 2));
        this.dateOrder = sb.toString();
    }
}
