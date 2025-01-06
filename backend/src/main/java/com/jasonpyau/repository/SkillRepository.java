package com.jasonpyau.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Skill;

@Repository
public interface SkillRepository extends JpaRepository<Skill, Integer> {
    @Query(value = "SELECT * FROM skills WHERE type = :type ORDER BY name ASC", nativeQuery = true)
    public List<Skill> findAllByTypeNameOrderedByName(@Param("type") String type);
    @Query(value = "SELECT * FROM skills WHERE name = :name", nativeQuery = true)
    public Optional<Skill> findByName(@Param("name") String name);
}
