package com.jasonpyau.service;

import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class SimpleIconsServiceTest {

    @InjectMocks
    private SimpleIconsService simpleIconsService;
    
    @Test
    public void getSimpleIconsSvgJava() {
        String svg = simpleIconsService.getSimpleIconsSvg("java");
        assertNotEquals(svg, SimpleIconsService.EMPTY_SVG);
        assertTrue(svg.contains("fill"));
    }

    @Test
    public void getSimpleIconsSvgSpringBoot() {
        String svg = simpleIconsService.getSimpleIconsSvg("springboot");
        assertNotEquals(svg, SimpleIconsService.EMPTY_SVG);
        assertTrue(svg.contains("fill"));
    }
}
