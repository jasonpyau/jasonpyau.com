package com.jasonpyau.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonpyau.entity.Message;
import com.jasonpyau.service.ContactService;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
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
    public void testSendMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/send")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isOk());
    }

    @Test
    public void testSendMessage_MessageNameError() throws Exception {
        String invalidName = "a".repeat(100);
        message.setName(invalidName);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/send")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.invalidFields.name", is(Message.MESSAGE_NAME_ERROR)));
    }
    
    @Test
    public void testSendMessage_MessageBodyError() throws Exception {
        String invalidBody = "";
        message.setBody(invalidBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/send")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.invalidFields.body", is(Message.MESSAGE_BODY_ERROR)));
    }

    @Test
    public void testSendMessage_MessageNameError_MessageBodyError() throws Exception {
        String invalidName = "a".repeat(100);
        String invalidBody = "";
        message.setName(invalidName);
        message.setBody(invalidBody);
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/send")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(message)))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.invalidFields.name", is(Message.MESSAGE_NAME_ERROR)))
            .andExpect(jsonPath("$.invalidFields.body", is(Message.MESSAGE_BODY_ERROR)));
    }

}
