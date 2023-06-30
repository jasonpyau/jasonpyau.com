package com.jasonpyau.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.jasonpyau.entity.Message;
import com.jasonpyau.repository.ContactRepository;
import com.jasonpyau.util.DateFormat;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Message message1 = new Message(1L, "Message1", "test1@gmail.com", "This is a test body of Message1", DateFormat.yyyyMMddHHmmss());
    
    @Test
    void testSendMessage_MessageNameError() {
        String invalidName = "a".repeat(100);
        Message message = new Message(2L, invalidName, "test3@gmail.com", "This is a test body of MessageTest", null);
        String errorMessage = contactService.sendMessage(message);
        assertEquals(ContactService.MESSAGE_NAME_ERROR, errorMessage);
    }

    @Test
    void sendMessage_MessageBodyError() {
        Message message = new Message(3L, "MessageTest", "test3@gmail.com", null, null);
        String errorMessage = contactService.sendMessage(message);
        assertEquals(ContactService.MESSAGE_BODY_ERROR, errorMessage);
    }

    @Test
    void testDeleteMessage() {
        given(contactRepository.findById(1L)).willReturn(Optional.of(message1));
        String errorMessage = contactService.deleteMessage(1L);
        assertEquals(null, errorMessage);
    }

    @Test
    void testDeleteMessage_Message_Id_Error() {
        String errorMessage = contactService.deleteMessage(2L);
        assertEquals(ContactService.MESSAGE_ID_ERROR, errorMessage);
    }

}
