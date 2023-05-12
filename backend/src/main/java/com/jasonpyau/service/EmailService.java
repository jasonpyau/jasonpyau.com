package com.jasonpyau.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    private static String fromEmail;
    private static String toEmail;
    
    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    @SuppressWarnings("static-access")
    public void setFromEmail(String fromEmail) {
        this.fromEmail = fromEmail;
    }

    @Value("${com.jasonpyau.email}")
    @SuppressWarnings("static-access")
    public void setToEmail(String toEmail) {
        this.toEmail = toEmail;
    }

    public static String toEmail() {
        return toEmail;
    }

    public void sendEmail(String toEmail, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromEmail);
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }
}
