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
import io.github.bucket4j.local.LocalBucketBuilder;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RateLimitService {

    // Single instance of RateLimitService
    public static final RateLimitService RateLimiter = new RateLimitService(150, 60, 20000, 600);

    // Time units in seconds
    private int requestsPerInterval;
    private int intervalDuration;

    private LoadingCache<String, Bucket> cache;

    private RateLimitService() {};

    private RateLimitService(int requestsPerInterval, int intervalDuration, int maximumCacheSize, int cacheDuration) {
        this.requestsPerInterval = requestsPerInterval;
        this.intervalDuration = intervalDuration;
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maximumCacheSize)
            .expireAfterWrite(cacheDuration, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Bucket>() {
                @Override
                public Bucket load(String key) {
                    return newBucket();
                }
            });
    }

    private Bandwidth getBandwidthLimit() {
        return Bandwidth.classic(requestsPerInterval, Refill.intervally(requestsPerInterval, Duration.ofSeconds(intervalDuration)));
    }

    private Bucket newBucket() {
        LocalBucketBuilder builder = Bucket.builder();
        builder.addLimit(getBandwidthLimit());
        return builder.build();
    }

    public ConsumptionProbe rateLimit(HttpServletRequest request, long token) {
        String key = UserService.getUserAddress(request);
        Bucket bucket = cache.getUnchecked(key);
        return bucket.tryConsumeAndReturnRemaining(token);
    }
}


