package com.jasonpyau.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Message;

@Repository
public interface ContactRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT m from Message m LEFT JOIN FETCH m.sender ORDER BY date DESC", nativeQuery = false)
    public Page<Message> findAllJoinedWithSenderWithPaginationOrderedByDate(Pageable pageable);
}
