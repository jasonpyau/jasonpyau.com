package com.jasonpyau.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class Authorization {

    private String password;
    private static String appPassword;

    @Value("${com.jasonpyau.appPassword}")
    @SuppressWarnings("static-access")
    public void setAppPassword(String appPassword) {
        this.appPassword = appPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean authorize() {
        return (password != null && !password.isBlank() && password.equals(appPassword));
    }
}
