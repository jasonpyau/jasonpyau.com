package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.User;
import com.jasonpyau.repository.UserRepository;
import com.jasonpyau.util.Hash;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User getDummyUser(HttpServletRequest request) {
        String hashedAddress = Hash.SHA256(request.getRemoteAddr()+" "+request.getLocalAddr());
        User user = new User();
        user.setAddress(hashedAddress);
        return user;
    }

    public User getUser(HttpServletRequest request) {
        String hashedAddress = Hash.SHA256(request.getRemoteAddr()+" "+request.getLocalAddr());
        Optional<User> optional = userRepository.findUserByAddress(hashedAddress);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            User user = new User();
            user.setAddress(hashedAddress);
            return user;
        }
    }

}
