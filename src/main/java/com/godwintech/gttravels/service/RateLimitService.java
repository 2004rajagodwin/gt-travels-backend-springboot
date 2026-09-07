package com.godwintech.gttravels.service;

import com.godwintech.gttravels.config.AuthProperties;
import com.godwintech.gttravels.exception.TooManyRequestsException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RateLimitService {

    private final AuthProperties authProperties;
    private final Map<String, WindowCounter> counters = new ConcurrentHashMap<>();

    public RateLimitService(AuthProperties authProperties) {
        this.authProperties = authProperties;
    }

    public void checkLimit(String key) {
        int limit = authProperties.getRateLimitPerMinute();
        long windowStart = Instant.now().getEpochSecond() / 60;
        String bucketKey = key + ":" + windowStart;

        WindowCounter counter = counters.computeIfAbsent(bucketKey, k -> new WindowCounter());
        int count = counter.incrementAndGet();

        if (count > limit) {
            throw new TooManyRequestsException("Too many requests. Please try again later.");
        }
    }

    private static class WindowCounter {
        private int count;

        synchronized int incrementAndGet() {
            return ++count;
        }
    }
}
