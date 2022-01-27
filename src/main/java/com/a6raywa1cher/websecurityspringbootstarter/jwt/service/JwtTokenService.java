package com.a6raywa1cher.websecurityspringbootstarter.jwt.service;

import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtToken;

import java.util.Optional;

public interface JwtTokenService {
    JwtToken issue(Long userId, Long refreshId);

    Optional<JwtToken> decode(String token);
}
