package com.jasonpyau.service;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.ConsumptionProbe;
import io.github.bucket4j.Refill;
import jakarta.servlet.http.HttpServletRequest;


@Service
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class RateLimitService {

    // Time units in seconds
    private Integer tokensPerInterval;
    private Integer intervalDuration;
    private final Integer cacheDuration = 600;

    // https://github.com/google/guava/wiki/cachesexplained
    private LoadingCache<String, Bucket> cache;

    private RateLimitService(@Value("${com.jasonpyau.rate-limit.tokens-per-interval:#{600}}") Integer tokensPerInterval,
                            @Value("${com.jasonpyau.rate-limit.interval-duration:#{30}}") Integer intervalDuration) {
        this.tokensPerInterval = tokensPerInterval;
        this.intervalDuration = intervalDuration;
        this.cache = CacheBuilder.newBuilder()
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

    // Cleans up expired cache entries (Users that have not accessed a token in 10 minutes), returning the new cache size.
    public long cleanCache() {
        cache.cleanUp();
        return cache.size();
    }
}


