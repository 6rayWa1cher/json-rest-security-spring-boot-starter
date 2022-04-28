package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The default implementation of {@link RefreshTokenService}.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.jwt.JwtAuthConfiguration
 */
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final RefreshTokenRepository repository;
	private final BlockedRefreshTokensService blockedTokensService;
	private final long maxTokensPerUser;
	private final Duration refreshTokenDuration;

	public RefreshTokenServiceImpl(RefreshTokenRepository repository, BlockedRefreshTokensService blockedTokensService,
								   @Value("${jwt.max-refresh-tokens-per-user}") Long maxTokensPerUser,
								   @Value("${jwt.refresh-duration}") Duration refreshTokenDuration) {
		this.repository = repository;
		this.blockedTokensService = blockedTokensService;
		this.maxTokensPerUser = maxTokensPerUser;
		this.refreshTokenDuration = refreshTokenDuration;
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public RefreshToken issue(IUser user) {
		List<RefreshToken> tokenList = repository.findAllByUser(user);
		if (tokenList.size() >= maxTokensPerUser) {
			repository.deleteAllFromUser(user, tokenList.stream()
				.sorted(Comparator.comparing(RefreshToken::getExpiringAt))
				.limit(tokenList.size() - maxTokensPerUser + 1)
				.peek(rt -> blockedTokensService.invalidate(user.getId(), rt.getId()))
				.toList());
		}
		RefreshToken refreshToken = RefreshToken.uuidToken(
			LocalDateTime.now().plus(refreshTokenDuration)
		);
		return repository.save(user, refreshToken);
	}

	@Override
	public Optional<RefreshToken> getByToken(IUser user, String token) {
		return repository.findByToken(user, token);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void invalidate(IUser user, RefreshToken refreshToken) {
		blockedTokensService.invalidate(user.getId(), refreshToken.getId());
		repository.delete(user, refreshToken);
	}

	@Override
	@Transactional(isolation = Isolation.SERIALIZABLE)
	public void invalidateAll(IUser user) {
		List<RefreshToken> allByUser = repository.findAllByUser(user);
		allByUser.forEach(rt -> blockedTokensService.invalidate(user.getId(), rt.getId()));
		repository.deleteAllFromUser(user, allByUser);
	}
}
