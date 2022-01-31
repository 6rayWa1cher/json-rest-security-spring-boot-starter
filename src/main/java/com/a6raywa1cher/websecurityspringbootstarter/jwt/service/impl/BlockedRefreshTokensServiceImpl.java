package com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl;

import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.time.Duration;

public class BlockedRefreshTokensServiceImpl implements BlockedRefreshTokensService {
    private final LoadingCache<String, String> cache;

    public BlockedRefreshTokensServiceImpl(Duration duration) {
        cache = CacheBuilder.newBuilder()
                .expireAfterWrite(duration)
                .build(new CacheLoader<>() {
                    @Override
                    public String load(String key) {
                        return key;
                    }
                });
    }

    @Override
    public void invalidate(String id) {
        cache.put(id, id);
    }

    @Override
    public boolean isValid(String id) {
        return cache.getIfPresent(id) == null;
    }
}
