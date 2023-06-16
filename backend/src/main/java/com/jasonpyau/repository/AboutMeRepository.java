package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.AboutMe;

@Repository
public interface AboutMeRepository extends CrudRepository<AboutMe, Integer> {
    
}
