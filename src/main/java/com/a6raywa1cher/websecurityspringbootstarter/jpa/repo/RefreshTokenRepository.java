package com.a6raywa1cher.websecurityspringbootstarter.jpa.repo;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
    List<RefreshToken> findAllByUser(AbstractUser user);

    void deleteAllFromUser(AbstractUser user, List<RefreshToken> listToDelete);

    RefreshToken save(AbstractUser user, RefreshToken refreshToken);

    Optional<RefreshToken> findByToken(AbstractUser user, String token);

    void delete(AbstractUser user, RefreshToken refreshToken);
}
