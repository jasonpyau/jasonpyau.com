package com.jasonpyau.entity;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    // Note: dates should be in form MM/YYYY
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Integer id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "start_date", nullable = false)
    private String startDate;
    @Column(name = "end_date", nullable = false)
    private String endDate;
    @Column(name = "date_order", nullable = true)
    private String dateOrder;
    @Column(name = "technologies", nullable = false)
    private List<String> technologies;
    @Column(name = "link", nullable = false)
    private String link;

    public static boolean checkDate(String date) {
        if (date == null) {
            return false;
        }
        String regex = "^(0[1-9]|1[0-2])/20[0-9]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(date);
        return matcher.matches();
    }

    public void createOrder() {
        if (startDate.length() != 7 || endDate.length() != 7) {
            System.out.println("WARNING: dates are not of correct length.");
            return;
        }
        StringBuilder sb = new StringBuilder(14);
        sb.append(startDate.substring(3));
        sb.append(startDate.substring(0, 2));
        sb.append(endDate.substring(3));
        sb.append(endDate.substring(0, 2));
        this.dateOrder = sb.toString();
    }
}
