package com.jasonpyau.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.jasonpyau.entity.Message;
import com.jasonpyau.exception.ResourceNotFoundException;
import com.jasonpyau.repository.ContactRepository;

@ExtendWith(MockitoExtension.class)
public class ContactServiceTest {

    @Mock
    private ContactRepository contactRepository;

    @InjectMocks
    private ContactService contactService;

    private Message message;

    @BeforeEach
    public void setUp() {
        this.message = Message.builder()
        .id(1L)
        .name("Jason Yau")
        .contactInfo("test@jasonpyau.com")
        .body("This is a test body of the message!")
        .date(null)
        .sender(null)
        .build();
    }

    @Test
    void testDeleteMessage() {
        given(contactRepository.findById(1L)).willReturn(Optional.of(message));
        assertDoesNotThrow(() -> {
            contactService.deleteMessage(1L);
        });
    }

    @Test
    void testDeleteMessage_Message_Id_Error() {
        ResourceNotFoundException e = assertThrows(ResourceNotFoundException.class, () -> {
            contactService.deleteMessage(2L);
        });
        assertEquals(Message.MESSAGE_ID_ERROR, e.getMessage());
    }

}
