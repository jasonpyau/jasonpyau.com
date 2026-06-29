package com.jasonpyau.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jasonpyau.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query(value = "SELECT * FROM users WHERE address = :address", nativeQuery = true)
    public Optional<User> findByAddress(@Param("address") String address);
}
