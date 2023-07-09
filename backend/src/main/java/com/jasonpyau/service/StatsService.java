package com.jasonpyau.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Stats;
import com.jasonpyau.repository.StatsRepository;
import com.jasonpyau.util.DateFormat;

@Service
public class StatsService {

    @Autowired
    private StatsRepository statsRepository;
    
    public Stats getStats() {
        Optional<Stats> optional = statsRepository.findById(1);
        if (!optional.isPresent()) {
            Stats stats = new Stats(1, DateFormat.MMddyyyy(), 0L);
            statsRepository.save(stats);
            return stats;
        }
        return optional.get();
    }

    public Stats updateLastUpdated() {
        String date = DateFormat.MMddyyyy();
        Stats stats = getStats();
        stats.setDate(date);
        return statsRepository.save(stats);
    }

    public Stats updateViews() {
        Stats stats = getStats();
        stats.setViews(stats.getViews()+1);
        return statsRepository.save(stats);
    }
}
