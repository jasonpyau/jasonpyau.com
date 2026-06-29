package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;

import com.jasonpyau.entity.AboutMe;

public interface AboutMeRepository extends CrudRepository<AboutMe, Integer> {
    
}
