package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "about_me")
public class AboutMe {

    @Id
    @Column(name = "id")
    private Integer id = 1;
    @Column(name = "text", columnDefinition = "varchar(1000)")
    private String text;

    public Integer getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }
}
