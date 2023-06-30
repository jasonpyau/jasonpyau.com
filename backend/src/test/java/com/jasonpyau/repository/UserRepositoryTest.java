package com.jasonpyau.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.jasonpyau.Application;
import com.jasonpyau.entity.User;
import com.jasonpyau.util.Hash;

import jakarta.transaction.Transactional;

@ExtendWith(SpringExtension.class)
@Transactional
@SpringBootTest(classes=Application.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    void testFindUserByAddress() {
        User user = new User();
        String hashedAddress = Hash.SHA256("localhost");
        user.setAddress(hashedAddress);
        userRepository.save(user);
        assertEquals(userRepository.findUserByAddress(hashedAddress).isPresent(), true);
    }
}
