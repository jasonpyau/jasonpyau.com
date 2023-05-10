package com.jasonpyau.repository;

import org.springframework.data.repository.CrudRepository;
import com.jasonpyau.model.Stats;

public interface StatsRepository extends CrudRepository<Stats, Integer> {
    
}
