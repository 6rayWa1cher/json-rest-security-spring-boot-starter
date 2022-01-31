package com.a6raywa1cher.websecurityspringbootstarter.component.criticalaction;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class CriticalActionLimiterService {
    private final LoadingCache<String, Integer> attemptsCache;

    private final int maxAttempts;

    public CriticalActionLimiterService(int maxAttempts, Duration blockDuration) {
        this.maxAttempts = maxAttempts;
        attemptsCache = CacheBuilder.newBuilder()
                .expireAfterWrite(blockDuration)
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void actionSucceed(String key) {
        attemptsCache.invalidate(key);
    }

    public void actionFailed(String key) {
        int attempts = attemptsCache.getUnchecked(key);
        attempts++;
        if (attempts == maxAttempts) {
            log.warn("Blocked bad-behaved user " + key);
        }
        attemptsCache.put(key, attempts);
    }

    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= maxAttempts;
    }
}