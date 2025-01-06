package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Message;
import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.form.PaginationForm;
import com.jasonpyau.repository.ContactRepository;
import com.jasonpyau.util.DateFormat;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ContactService {
    
    @Autowired
    private ContactRepository contactRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserService userService;

    public void sendMessage(HttpServletRequest request, Message message) {
        String name = message.getName();
        String contactInfo = message.getContactInfo();
        String body = message.getBody();
        message.setDate(DateFormat.yyyyMMddHHmmss());
        message.setSender(userService.getUser(request));
        contactRepository.save(message);
        String emailSubject = name + " sent you a message";
        String emailBody = "Name: " + name + "\n" +
                            "Contact Info: " + contactInfo + "\n\n" +
                            body + "\n";
        emailService.sendEmail(EmailService.toEmail(), emailSubject, emailBody);
    }

    public Page<Message> getMessages(PaginationForm paginationForm) {
        Pageable pageable = PageRequest.of(paginationForm.getPageNum(), paginationForm.getPageSize());
        Page<Message> page = contactRepository.findAllJoinedWithSenderWithPaginationOrderedByDate(pageable);
        return page;
    }

    public void deleteMessage(Long id) {
        Optional<Message> optional = contactRepository.findById(id);
        if (!optional.isPresent()) {
            throw new ResourceNotFoundException(Message.MESSAGE_ID_ERROR);
        }
        contactRepository.delete(optional.get());
    }

}
