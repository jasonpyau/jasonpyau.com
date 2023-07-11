package com.jasonpyau.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.Builder;

@Service
public class RateLimitService {

    // Single instance of RateLimitService
    public static final RateLimitService RateLimiter = RateLimitService.builder()
                                                        .tokensPerInterval(150)
                                                        .intervalDuration(60)
                                                        .maximumCacheSize(15000)
                                                        .cacheDuration(600)
                                                        .build();

    // Time units in seconds
    private int tokensPerInterval;
    private int intervalDuration;

    private LoadingCache<String, Bucket> cache;

    private RateLimitService() {};

    @Builder(access = AccessLevel.PRIVATE)
    private RateLimitService(int tokensPerInterval, int intervalDuration, int maximumCacheSize, int cacheDuration) {
        this.tokensPerInterval = tokensPerInterval;
        this.intervalDuration = intervalDuration;
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maximumCacheSize)
            .expireAfterAccess(cacheDuration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Bucket>() {
                @Override
                public Bucket load(String key) {
                    return newBucket();
                }
            });
    }

    private Bandwidth getBandwidthLimit() {
        return Bandwidth.classic(tokensPerInterval, Refill.intervally(tokensPerInterval, Duration.ofSeconds(intervalDuration)));
    }

    private Bucket newBucket() {
        return Bucket.builder()
                    .addLimit(getBandwidthLimit())
                    .build();
    }

    public ConsumptionProbe rateLimit(HttpServletRequest request, long token) {
        String key = UserService.getUserAddress(request);
        Bucket bucket = cache.getUnchecked(key);
        return bucket.tryConsumeAndReturnRemaining(token);
    }
}


