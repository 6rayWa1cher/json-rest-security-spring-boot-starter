package com.a6raywa1cher.jsonrestsecurity.criticalaction;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;

import java.time.Duration;

public class CriticalActionLimiterService {
    private static final Logger log = org.slf4j.LoggerFactory.getLogger(CriticalActionLimiterService.class);
    private final LoadingCache<String, Integer> attemptsCache;

    private final int maxAttempts;

    public CriticalActionLimiterService(int maxAttempts, Duration blockDuration) {
        this.maxAttempts = maxAttempts;
        attemptsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(blockDuration)
			.softValues()
                .build(new CacheLoader<>() {
                    @Override
                    public Integer load(String key) {
                        return 0;
                    }
                });
    }

    public void actionFailed(String key) {
		int attempts = attemptsCache.getUnchecked(key);
		if (attempts < maxAttempts) {
			attemptsCache.asMap().compute(key, (k, v) -> v == null ? 1 : v + 1);
		} else if (attempts == maxAttempts) {
			log.warn("Blocked bad-behaved user " + key);
		}
	}

    public boolean isBlocked(String key) {
        return attemptsCache.getUnchecked(key) >= maxAttempts;
    }
}