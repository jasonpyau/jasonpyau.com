package com.jasonpyau.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;

import com.jasonpyau.entity.Link;
import com.jasonpyau.repository.LinkRepository;
import com.jasonpyau.util.CacheUtil;
import com.jasonpyau.util.DateFormat;

@Service
public class LinkService {
    
    @Autowired
    private LinkRepository linkRepository;

    @CacheEvict(cacheNames = CacheUtil.LINK_CACHE, allEntries = true)
    public void newLink(Link link) {
        link.setLastUpdatedUnixTime(DateFormat.getUnixTime());
        linkRepository.save(link);
    }
}
