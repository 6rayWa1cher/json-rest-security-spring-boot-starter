package com.a6raywa1cher.websecurityspringbootstarter.jwt.service;


import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    RefreshToken issue(AbstractUser user);

    Optional<RefreshToken> getByToken(AbstractUser user, String token);

    void invalidate(AbstractUser user, RefreshToken refreshToken);

    void invalidateAll(AbstractUser user);
}
