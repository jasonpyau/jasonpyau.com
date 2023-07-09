package com.jasonpyau.controller;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jasonpyau.entity.Message;
import com.jasonpyau.service.ContactService;
import com.jasonpyau.util.DateFormat;

@WebMvcTest(ContactController.class)
public class ContactControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ContactService contactService;

    private Message message1 = new Message(1L, "Message1", "test1@gmail.com", "This is a test body of Message1", DateFormat.yyyyMMddHHmmss());

    @Test
    public void testSendMessage() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/contact/send")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(message1)))
            .andExpect(status().isOk());
    }

    @Test
    public void testSendMessage_MessageNameError() throws Exception {
        String invalidName = "a".repeat(100);
        Message message = new Message(2L, invalidName, "test3@gmail.com", "This is a test body of MessageTest", null);
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
        Message message = new Message(3L, "MessageTest", "test3@gmail.com", invalidBody, null);
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
        Message message = new Message(3L, invalidName, "test3@gmail.com", invalidBody, null);
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
