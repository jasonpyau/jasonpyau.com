package com.jasonpyau.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Link;

@Repository
public interface LinkRepository extends JpaRepository<Link, Integer> {
    @Query(value = "SELECT * from links ORDER BY last_updated_unix_time DESC", nativeQuery = true)
    public List<Link> findAllOrderedByLastUpdatedUnixTime();
}
