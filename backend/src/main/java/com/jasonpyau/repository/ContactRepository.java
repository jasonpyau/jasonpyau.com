package com.jasonpyau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Message;

@Repository
public interface ContactRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM messages ORDER BY date DESC", nativeQuery = true)
    Page<Message> findAllWithPaginationOrderedByDate(Pageable pageable);
}
