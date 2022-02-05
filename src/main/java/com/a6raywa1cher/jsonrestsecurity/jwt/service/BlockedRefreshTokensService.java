package com.a6raywa1cher.jsonrestsecurity.jwt.service;

import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.LoadingCacheBlockedRefreshTokensService;

/**
 * Holds recently invalidated refresh tokens.
 * <br/>
 * Should contain invalidated refresh token during the access tokens maximum lifetime.
 *
 * @see LoadingCacheBlockedRefreshTokensService
 */
public interface BlockedRefreshTokensService {
	/**
	 * Invalidate the refresh token by id
	 *
	 * @param id refresh token id
	 */
	void invalidate(String id);

	/**
	 * Checks if the refresh token was recently revoked
	 *
	 * @param id refresh token id
	 * @return true if the refresh token was recently revoked else false
	 */
	boolean isValid(String id);
}
