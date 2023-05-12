package com.jasonpyau.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Stats {

    @Id
    private Integer id = 1;
    private String date;
    private Long views;

    public Integer getId() { 
        return this.id;
    }

    public String getDate() {
        return this.date;
    }

    public Long getViews() {
        return this.views;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setViews(Long views) {
        this.views = views;
    }
}
