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

@Entity
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

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public List<String> getTechnologies() {
        return technologies;
    }

    public String getLink() {
        return link;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public void setDateOrder(String order) {
        this.dateOrder = order;
    }

    public void setTechnologies(List<String> technologies) {
        this.technologies = technologies;
    }

    public void setLink(String link) {
        this.link = link;
    }

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
