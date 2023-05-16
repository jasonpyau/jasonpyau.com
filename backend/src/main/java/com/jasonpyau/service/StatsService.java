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
        if (!optional.isPresent())
            return null;
        return optional.get();
    }

    public Stats updateLastUpdated() {
        String date = DateFormat.MMddyyyy();
        Stats stats = getStats();
        if (stats == null)
            return null;
        stats.setDate(date);
        statsRepository.save(stats);
        return stats;
    }

    public Stats updateViews() {
        Stats stats = getStats();
        if (stats == null)
            return null;
        Long currViews = (stats.getViews() == null) ? 0 : stats.getViews();
        stats.setViews(currViews+1);
        statsRepository.save(stats);
        return stats;
    }
}
