package com.jasonpyau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jasonpyau.entity.Experience;

public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query(value = "SELECT * FROM experiences WHERE type = :type ORDER BY date_order DESC", nativeQuery = true)
    public List<Experience> findAllByTypeNameOrderedByDate(@Param("type") String type);
}
