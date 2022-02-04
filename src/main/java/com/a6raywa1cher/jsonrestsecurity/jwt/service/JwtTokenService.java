package com.a6raywa1cher.jsonrestsecurity.jwt.service;

import com.a6raywa1cher.jsonrestsecurity.jwt.JwtToken;

import java.util.Optional;

public interface JwtTokenService {
    JwtToken issue(Long userId, String refreshId);

    Optional<JwtToken> decode(String token);
}
