package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.User;
import com.jasonpyau.repository.UserRepository;
import com.jasonpyau.util.Hash;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private static String activeProfile;

    @Value("${spring.profiles.active:#{null}}")
    @SuppressWarnings("static-access")
    public void setActiveProfile(String activeProfile) {
        this.activeProfile = activeProfile;
    }

    // Use if you want to get the user without querying the database and creating a new user (if the user doesn't exist).
    public User getDummyUser(HttpServletRequest request) {
        String hashedAddress = getUserAddress(request);
        User user = new User();
        user.setAddress(hashedAddress);
        return user;
    }

    public User getUser(HttpServletRequest request) {
        String hashedAddress = getUserAddress(request);
        Optional<User> optional = userRepository.findUserByAddress(hashedAddress);
        if (optional.isPresent()) {
            return optional.get();
        } else {
            User user = new User();
            user.setAddress(hashedAddress);
            return userRepository.save(user);
        }
    }

    // Since in production, Cloudflare goes through every connection.
    public static String getUserAddress(HttpServletRequest request) {
        if (activeProfile == null) {
            return Hash.SHA256(request.getRemoteAddr()+" "+request.getLocalAddr());
        }
        switch (activeProfile) {
            case "production":
                return Hash.SHA256(request.getHeader("CF-Connecting-IP"));
            case "dev":
            case "default":
            default:
                return Hash.SHA256(request.getRemoteAddr()+" "+request.getLocalAddr());
        }
    }

}
