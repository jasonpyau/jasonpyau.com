package com.jasonpyau.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class AuthorizationService {

    private static String appPassword;

    @Value("${com.jasonpyau.app-password}")
    @SuppressWarnings("static-access")
    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    private AuthorizationService() {}

    public static boolean authorize(HttpServletRequest request) {
        if (request == null) {
            return false;
        }
        String password = request.getHeader("Authorization");
        return (password != null && !password.isBlank() && password.equals(appPassword));
    }
}
