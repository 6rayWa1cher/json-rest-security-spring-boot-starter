package com.a6raywa1cher.jsonrestsecurity.faillimiter;


import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;

import static com.a6raywa1cher.jsonrestsecurity.utils.LogUtils.log;

/**
 * The default implementation of {@link FailLimiterService}.
 * <br/>
 * Based on {@link LoadingCache} from Guava with soft values. After the block time has passed, the client's
 * cached count of failed attempts is expired, and the {@link #attemptsCache} will remove it according to
 * {@link LoadingCache} processes.
 * <br/>
 * By default, a client has 0 failed attempts. Each {@link #actionFailed(String)} invocation
 * append one to failed attempts count. If that count exceeds the maximum, the client becomes blocked, and
 * {@link #isBlocked(String)} will return {@code true} during the block time.
 * <br/>
 * After the block time has passed, the client is no longer blocked. But if the client performs
 * requests while blocked, block time will reset.
 * <br/>
 *
 * @see LoadingCache
 * @see FailLimiterConfiguration
 */
public class LoadingCacheFailLimiterService implements FailLimiterService {
	private final LoadingCache<String, Integer> attemptsCache;

	private final int maxAttempts;

	public LoadingCacheFailLimiterService(int maxAttempts, Duration blockDuration, int maxSize) {
		this.maxAttempts = maxAttempts;
		attemptsCache = CacheBuilder.newBuilder()
			.expireAfterWrite(blockDuration)
			.softValues()
			.maximumSize(maxSize)
			.removalListener((n) -> log.info("Unblocked user " + n.getKey()))
			.build(new CacheLoader<>() {
				@Override
				public Integer load(String key) {
					return 0;
				}
			});
	}

	@Override
	public void actionFailed(String key) {
		int attempts = attemptsCache.getUnchecked(key);
		if (attempts < maxAttempts) {
			attemptsCache.asMap().compute(key, (k, v) -> v == null ? 1 : v >= maxAttempts ? maxAttempts : v + 1);
		} else if (attempts == maxAttempts) {
			log.warn("Blocked bad-behaved user " + key);
		}
	}

	@Override
	public boolean isBlocked(String key) {
		return attemptsCache.getUnchecked(key) >= maxAttempts;
	}
}