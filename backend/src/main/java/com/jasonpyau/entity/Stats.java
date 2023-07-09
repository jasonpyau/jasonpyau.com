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
@Table(name = "stats")
public class Stats {

    @Id
    @Column(name = "id")
    private Integer id = 1;

    @Column(name = "date")
    private String date;
    
    @Column(name = "views")
    private Long views;

}
