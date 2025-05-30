package com.jasonpyau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Experience;

@Repository
public interface ExperienceRepository extends JpaRepository<Experience, Integer> {
    @Query(value = "SELECT * FROM experiences WHERE type = :type ORDER BY date_order DESC", nativeQuery = true)
    public List<Experience> findAllByTypeNameOrderedByDate(@Param("type") String type);
}
