package com.jasonpyau.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Message;
import com.jasonpyau.repository.ContactRepository;
import com.jasonpyau.util.DateFormat;

@Service
public class ContactService {
    
    private static final String MESSAGE_NAME_ERROR = "Name should be between 3-50 characters.";
    private static final String MESSAGE_CONTACT_INFO_ERROR = "Contact Info should be between 6-100 characters.";
    private static final String MESSAGE_BODY_ERROR = "Body should be between 15-3000 characters.";
    
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailService emailService;

    // Returns error message if applicable, else null.
    public String sendMessage(Message message) {
        String name = message.getName();
        String contactInfo = message.getContactInfo();
        String body = message.getBody();
        message.setDate(DateFormat.dateTime());
        if (name == null || name.length() < 3 || name.length() > 50 || name.isBlank()) {
            return MESSAGE_NAME_ERROR;
        } else if (contactInfo == null || contactInfo.length() < 6 || contactInfo.length() > 100 || contactInfo.isBlank()) {
            return MESSAGE_CONTACT_INFO_ERROR;
        } else if (body == null || body.length() < 15 || body.length() > 3000 || body.isBlank()) {
            return MESSAGE_BODY_ERROR;
        }
        contactRepository.save(message);
        String emailSubject = name + " sent you a message";
        String emailBody = "Name: " + name + "\n" +
                            "Contact Info: " + contactInfo + "\n\n" +
                            body + "\n";
        emailService.sendEmail(EmailService.toEmail(), emailSubject, emailBody);
        return null;
    }

    public List<Message> getMessages(int pageNum, int pageSize) {
        Sort sort = Sort.by("id").descending();
        Pageable pageable = PageRequest.of(pageNum, pageSize, sort);
        Page<Message> page = contactRepository.findAll(pageable);
        return page.getContent();
    }
}
