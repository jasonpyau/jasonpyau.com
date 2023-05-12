package com.jasonpyau.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.jasonpyau.entity.Message;

@Repository
public interface ContactRepository extends JpaRepository<Message, Long> {
    
}
