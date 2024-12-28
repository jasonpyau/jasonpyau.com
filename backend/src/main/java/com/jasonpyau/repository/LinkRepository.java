package com.jasonpyau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    
}
