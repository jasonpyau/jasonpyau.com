package com.jasonpyau.util;

import java.io.IOException;
import java.net.URL;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class SkillIconSvgData {

    public static JsonNode get() {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readTree(new URL("https://raw.githubusercontent.com/simple-icons/simple-icons/6.23.0/_data/simple-icons.json")).get("icons");
        } catch (IOException e) {
            return objectMapper.createObjectNode();
        }
    }
}
