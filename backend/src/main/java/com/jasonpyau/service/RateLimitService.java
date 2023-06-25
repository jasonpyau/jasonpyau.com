package com.jasonpyau.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import io.github.bucket4j.local.LocalBucketBuilder;
import jakarta.servlet.http.HttpServletRequest;

@Service
public class RateLimitService {
    
    public static final int DEFAULT_TYPE = 1;
    public static final int ADMIN_TYPE = 2;
    public static final int SEND_MESSAGES_TYPE = 3;

    // Singleton-like instances of RateLimitService
    public static final RateLimitService rateLimitService = new RateLimitService(DEFAULT_TYPE);
    public static final RateLimitService adminRateLimitService = new RateLimitService(ADMIN_TYPE);

    private int requestsPerInterval;
    private int durationInSeconds;
    private int maximumCacheSize;
    private LoadingCache<String, Bucket> cache;

    public RateLimitService() {
        this(DEFAULT_TYPE);
    }

    public RateLimitService(int type) {
        switch(type) {
            case ADMIN_TYPE:
                this.requestsPerInterval = 50;
                this.durationInSeconds = 20;
                this.maximumCacheSize = 100;
                break;
            case SEND_MESSAGES_TYPE:
                this.requestsPerInterval = 4;
                this.durationInSeconds = 30;
                this.maximumCacheSize = 10000;
                break;
            case DEFAULT_TYPE:
            default:
                this.requestsPerInterval = 40;
                this.durationInSeconds = 20;
                this.maximumCacheSize = 10000;
        }
        this.cache = CacheBuilder.newBuilder()
            .maximumSize(maximumCacheSize)
            .expireAfterWrite(durationInSeconds, TimeUnit.SECONDS)
            .build(new CacheLoader<String, Bucket>() {
                @Override
                public Bucket load(String key) {
                    return newBucket();
                }
            });
    }

    public int getRequestsPerInterval() {
        return requestsPerInterval;
    }

    public int getDurationInSeconds() {
        return durationInSeconds;
    }

    private Bandwidth getBandwidthLimit() {
        return Bandwidth.classic(requestsPerInterval, Refill.intervally(requestsPerInterval, Duration.ofSeconds(durationInSeconds)));
    }

    private Bucket newBucket() {
        LocalBucketBuilder builder = Bucket.builder();
        builder.addLimit(getBandwidthLimit());
        return builder.build();
    }

    // Return true if user is rate limited, false otherwise.
    public boolean rateLimit(HttpServletRequest request) {
        String key = request.getRemoteAddr();
        Bucket bucket = cache.getUnchecked(key);
        boolean rateLimited = true;
        if (bucket.tryConsume(1)) {
            rateLimited = false;
        }
        return rateLimited;
    }
}


