package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Stats;

@Repository
public interface StatsRepository extends CrudRepository<Stats, Integer> {
    
}
