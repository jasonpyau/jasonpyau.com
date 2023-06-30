package com.jasonpyau.service;

import static org.mockito.BDDMockito.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.jasonpyau.entity.Stats;
import com.jasonpyau.repository.StatsRepository;
import com.jasonpyau.util.DateFormat;

@ExtendWith(MockitoExtension.class)
public class StatsServiceTest {
    
    @Mock
    private StatsRepository statsRepository;

    @InjectMocks
    private StatsService statsService;

    private Stats originalStats;
    
    @BeforeEach
    public void setUp() {
        originalStats = new Stats(1, "06/25/2023", 999L);
    }

    @Test
    void testUpdateViews() {
        Stats dummy = new Stats(originalStats.getId(), originalStats.getDate(), originalStats.getViews());
        given(statsRepository.findById(1)).willReturn(Optional.of(dummy));
        Stats stats = statsService.updateViews();
        assertEquals(originalStats.getId(), stats.getId());
        assertEquals(originalStats.getViews()+1, stats.getViews());
        assertEquals(originalStats.getDate(), stats.getDate());
    }

    @Test
    void testUpdateLastUpdated() {
        Stats dummy = new Stats(originalStats.getId(), originalStats.getDate(), originalStats.getViews());
        given(statsRepository.findById(1)).willReturn(Optional.of(dummy));
        Stats stats = statsService.updateLastUpdated();
        assertEquals(originalStats.getId(), stats.getId());
        assertEquals(originalStats.getViews(), stats.getViews());
        assertEquals(DateFormat.MMddyyyy(), stats.getDate());
    }
}
