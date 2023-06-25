package com.jasonpyau.util;

import java.nio.charset.StandardCharsets;

import com.google.common.hash.Hashing;

public class Hash {
    
    private Hash() {}

    public static String SHA256(String originalString) {
        return Hashing.sha256().hashString(originalString, StandardCharsets.UTF_8).toString();
    }
}
