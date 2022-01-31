package com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl;

import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.BlockedRefreshTokensService;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class BlockedRefreshTokensServiceImpl implements BlockedRefreshTokensService {
    private final LoadingCache<String, String> cache;

    @Autowired
    public BlockedRefreshTokensServiceImpl(@Value("${jwt.access-duration}") Duration duration) {
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
