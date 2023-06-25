package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Message;
import com.jasonpyau.repository.ContactRepository;
import com.jasonpyau.util.DateFormat;

@Service
public class ContactService {
    
    private static final String MESSAGE_ID_ERROR = "Invalid 'id', message not found.";
    private static final String MESSAGE_NAME_ERROR = "'name' should be between 3-50 characters.";
    private static final String MESSAGE_CONTACT_INFO_ERROR = "'contactInfo' should be between 6-100 characters.";
    private static final String MESSAGE_BODY_ERROR = "'body' should be between 15-1000 characters.";
    
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailService emailService;

    // Returns error message if applicable, else null.
    public String sendMessage(Message message) {
        String name = message.getName();
        String contactInfo = message.getContactInfo();
        String body = message.getBody();
        message.setDate(DateFormat.yyyyMMddHHmmss());
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

    public Page<Message> getMessages(int pageNum, int pageSize) {
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        Page<Message> page = contactRepository.findAllWithPaginationOrderedByDate(pageable);
        return page;
    }

    // Returns error message if applicable, else null.
    public String deleteMessage(Long id) {
        Optional<Message> optional = contactRepository.findById(id);
        if (!optional.isPresent()) {
            return MESSAGE_ID_ERROR;
        }
        contactRepository.delete(optional.get());
        return null;
    }

}
