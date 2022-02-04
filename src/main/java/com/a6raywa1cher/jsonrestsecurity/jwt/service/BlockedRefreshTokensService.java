package com.a6raywa1cher.jsonrestsecurity.jwt.service;

public interface BlockedRefreshTokensService {
    void invalidate(String id);

    boolean isValid(String id);
}
