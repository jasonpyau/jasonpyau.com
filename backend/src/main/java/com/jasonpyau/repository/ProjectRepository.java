package com.jasonpyau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Integer> {
    @Query(value = "SELECT * FROM projects ORDER BY date_order DESC", nativeQuery = true)
    public List<Project> findAllByDate();
}
