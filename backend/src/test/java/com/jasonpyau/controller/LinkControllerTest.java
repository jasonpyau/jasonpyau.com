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
import com.jasonpyau.entity.Link;
import com.jasonpyau.service.LinkService;
import com.jasonpyau.util.DateFormat;

@WebMvcTest(LinkController.class)
public class LinkControllerTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private LinkService linkService;

    private Link link;
    
    @BeforeEach
    public void setUp() {
        link = Link.builder()
                    .id(1)
                    .name("GitHub")
                    .href("https://github.com/jasonpyau")
                    .simpleIconsIconSlug("github")
                    .hexFill("#ffffff")
                    .lastUpdatedUnixTime(DateFormat.getUnixTime())
                    .build();
    }

    @Test
    public void testNewLink() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/links/new")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(link)))
            .andExpect(status().isOk());
    }

    @Test
    public void testNewLink_LinkHrefError() throws Exception {
        link.setHref("github.com/jasonpyau");
        mockMvc.perform(MockMvcRequestBuilders.post("/links/new")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(link)))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.invalidFields.href", is(Link.LINK_HREF_ERROR)));
    }

    @Test
    public void testNewLink_LinkHexFillError() throws Exception {
        link.setHexFill("#fffggg");
        mockMvc.perform(MockMvcRequestBuilders.post("/links/new")
            .header("X-Forwarded-For", "localhost")
            .header("CF-Connecting-IP", "localhost")
            .contentType("application/json")
            .content(objectMapper.writeValueAsString(link)))
            .andExpect(status().isNotAcceptable())
            .andExpect(jsonPath("$.invalidFields.hexFill", is(Link.LINK_HEX_FILL_ERROR)));
    }
}
