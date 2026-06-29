package com.jasonpyau.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jasonpyau.entity.Skill;

public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query(value = "SELECT * FROM skills WHERE type = :type ORDER BY name ASC", nativeQuery = true)
    public List<Skill> findAllByTypeNameOrderedByName(@Param("type") String type);
    @Query(value = "SELECT * FROM skills WHERE name = :name", nativeQuery = true)
    public Optional<Skill> findByName(@Param("name") String name);
}
