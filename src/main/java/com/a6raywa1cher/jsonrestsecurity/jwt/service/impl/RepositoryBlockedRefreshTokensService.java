package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.IUserRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.google.common.cache.LoadingCache;

import java.util.Optional;

/**
 * The default implementation of {@link BlockedRefreshTokensService}.
 * <br/>
 * Uses {@link LoadingCache} for storage. Can potentially crash the JVM on a DoS attack with {@code OutOfMemoryError}.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration
 */
public class RepositoryBlockedRefreshTokensService<T extends IUser> implements BlockedRefreshTokensService {
	private final IUserRepository<T> userRepository;

	public RepositoryBlockedRefreshTokensService(IUserRepository<T> userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public void invalidate(Long userId, String id) {

	}

	@Override
	public boolean isValid(Long userId, String id) {
		Optional<T> user = userRepository.findById(userId);
		return user
			.stream()
			.flatMap(u -> u.getRefreshTokens().stream())
			.anyMatch(t -> id.equals(t.getId()));
	}
}
