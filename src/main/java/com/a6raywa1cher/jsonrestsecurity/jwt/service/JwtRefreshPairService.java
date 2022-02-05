package com.a6raywa1cher.jsonrestsecurity.jwt.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtRefreshPair;

/**
 * Issuer of access/refresh paired tokens
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.JwtRefreshPairServiceImpl
 */
public interface JwtRefreshPairService {
	/**
	 * Issue a new pair of access and refresh tokens
	 *
	 * @param user owner of new tokens
	 * @return access and refresh tokens pair
	 */
	JwtRefreshPair issue(IUser user);
}
