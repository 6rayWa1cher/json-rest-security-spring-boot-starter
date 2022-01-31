package com.a6raywa1cher.websecurityspringbootstarter.jwt.service.impl;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.RefreshToken;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtRefreshPair;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtToken;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.RefreshTokenService;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;

@Service
public class JwtRefreshPairServiceImpl implements JwtRefreshPairService {
    private final RefreshTokenService refreshTokenService;

    private final JwtTokenService jwtTokenService;

    public JwtRefreshPairServiceImpl(RefreshTokenService refreshTokenService, JwtTokenService jwtTokenService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public JwtRefreshPair issue(AbstractUser user) {
        RefreshToken refreshToken = refreshTokenService.issue(user);
        JwtToken accessToken = jwtTokenService.issue(user.getId(), refreshToken.id());
        return new JwtRefreshPair(
                refreshToken.token(),
                OffsetDateTime.of(refreshToken.expiringAt(), OffsetDateTime.now().getOffset()),
                accessToken.getToken(),
                OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset())
        );
    }
}
