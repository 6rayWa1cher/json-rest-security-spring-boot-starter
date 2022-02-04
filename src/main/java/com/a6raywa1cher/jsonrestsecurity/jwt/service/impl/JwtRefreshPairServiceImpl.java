package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtRefreshPair;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtToken;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;

import java.time.OffsetDateTime;

public class JwtRefreshPairServiceImpl implements JwtRefreshPairService {
    private final RefreshTokenService refreshTokenService;

    private final JwtTokenService jwtTokenService;

    public JwtRefreshPairServiceImpl(RefreshTokenService refreshTokenService, JwtTokenService jwtTokenService) {
        this.refreshTokenService = refreshTokenService;
        this.jwtTokenService = jwtTokenService;
    }

    @Override
    public JwtRefreshPair issue(IUser user) {
        RefreshToken refreshToken = refreshTokenService.issue(user);
        JwtToken accessToken = jwtTokenService.issue(user.getId(), refreshToken.id());
        return new JwtRefreshPair(
			refreshToken.token(),
			OffsetDateTime.of(refreshToken.expiringAt(), OffsetDateTime.now().getOffset()),
			accessToken.getToken(),
			OffsetDateTime.of(accessToken.getExpiringAt(), OffsetDateTime.now().getOffset()),
			user.getId()
		);
    }
}
