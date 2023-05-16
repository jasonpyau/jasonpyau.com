package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "stats")
public class Stats {

    @Id
    @Column(name = "id")
    private Integer id = 1;
    @Column(name = "date")
    private String date;
    @Column(name = "views")
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
