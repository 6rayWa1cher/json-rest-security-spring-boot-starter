package com.a6raywa1cher.websecurityspringbootstarter.jwt.service;

public interface BlockedRefreshTokensService {
    void invalidate(Long id);

    boolean isValid(Long id);
}
