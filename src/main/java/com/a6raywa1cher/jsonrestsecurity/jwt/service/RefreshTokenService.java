package com.a6raywa1cher.jsonrestsecurity.jwt.service;


import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;

import java.util.Optional;

/**
 * Provides methods to operate refresh tokens
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.RefreshTokenServiceImpl
 */
public interface RefreshTokenService {
	/**
	 * Issues a new refresh token and returns it in {@link RefreshToken}
	 *
	 * @param user owner of the refresh token
	 * @return refresh token and metadata
	 */
	RefreshToken issue(IUser user);

	/**
	 * Extracts the refresh token's metadata
	 *
	 * @param user  owner of the refresh token
	 * @param token refresh token
	 * @return refresh token and metadata
	 */
	Optional<RefreshToken> getByToken(IUser user, String token);

	/**
	 * Invalidates the refresh token and corresponding access tokens
	 *
	 * @param user         owner of the refresh token
	 * @param refreshToken refresh token
	 */
	void invalidate(IUser user, RefreshToken refreshToken);

	/**
	 * Invalidates all access and refresh tokens of the user
	 *
	 * @param user user whose tokens will be invalidated
	 */
	void invalidateAll(IUser user);
}
