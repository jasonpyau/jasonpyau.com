package com.jasonpyau.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
@Table(name = "about_me")
public class AboutMe {

    @Id
    @Column(name = "id")
    private Integer id = 1;
    @Column(name = "text", columnDefinition = "varchar(1000)")
    private String text;
    
}
