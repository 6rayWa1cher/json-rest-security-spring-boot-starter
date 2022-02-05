package com.a6raywa1cher.jsonrestsecurity.jwt.service;


import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
	RefreshToken issue(IUser user);

	Optional<RefreshToken> getByToken(IUser user, String token);

	void invalidate(IUser user, RefreshToken refreshToken);

	void invalidateAll(IUser user);
}
