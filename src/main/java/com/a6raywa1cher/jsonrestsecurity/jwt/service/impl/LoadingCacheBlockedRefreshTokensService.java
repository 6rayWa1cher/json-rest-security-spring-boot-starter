package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;

/**
 * The alternative implementation of {@link BlockedRefreshTokensService}.
 * <br/>
 * Uses {@link LoadingCache} for storage. Can potentially crash the JVM on a DoS attack with {@code OutOfMemoryError}.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration
 */
public class LoadingCacheBlockedRefreshTokensService implements BlockedRefreshTokensService {
	private final LoadingCache<String, String> cache;

	public LoadingCacheBlockedRefreshTokensService(Duration duration) {
		cache = CacheBuilder.newBuilder()
			.expireAfterWrite(duration)
			.build(new CacheLoader<>() {
				@Override
				@SuppressWarnings("NullableProblems")
				public String load(String key) {
					return key;
				}
			});
	}

	@Override
	public void invalidate(Long userId, String id) {
		cache.put(id, id);
	}

	@Override
	public boolean isValid(Long userId, String id) {
		return cache.getIfPresent(id) == null;
	}
}
