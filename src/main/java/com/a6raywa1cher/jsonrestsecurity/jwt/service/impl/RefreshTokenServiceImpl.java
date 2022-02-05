package com.a6raywa1cher.jsonrestsecurity.jwt.service.impl;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {
	private final RefreshTokenRepository repository;
	private final BlockedRefreshTokensService service;
	private final long maxTokensPerUser;
	private final Duration refreshTokenDuration;

	public RefreshTokenServiceImpl(RefreshTokenRepository repository, BlockedRefreshTokensService service,
								   @Value("${jwt.max-refresh-tokens-per-user}") Long maxTokensPerUser,
								   @Value("${jwt.refresh-duration}") Duration refreshTokenDuration) {
		this.repository = repository;
		this.service = service;
		this.maxTokensPerUser = maxTokensPerUser;
		this.refreshTokenDuration = refreshTokenDuration;
	}

	@Override
	public RefreshToken issue(IUser user) {
		List<RefreshToken> tokenList = repository.findAllByUser(user);
		if (tokenList.size() > maxTokensPerUser) {
			repository.deleteAllFromUser(user, tokenList.stream()
				.sorted(Comparator.comparing(RefreshToken::expiringAt))
				.limit(tokenList.size() - 5)
				.peek(rt -> service.invalidate(rt.id()))
				.toList());
		}
		RefreshToken refreshToken = new RefreshToken(
			UUID.randomUUID().toString(),
			UUID.randomUUID().toString(),
			LocalDateTime.now().plus(refreshTokenDuration)
		);
		return repository.save(user, refreshToken);
	}

	@Override
	public Optional<RefreshToken> getByToken(IUser user, String token) {
		return repository.findByToken(user, token);
	}

	@Override
	public void invalidate(IUser user, RefreshToken refreshToken) {
		service.invalidate(refreshToken.id());
		repository.delete(user, refreshToken);
	}

	@Override
	@Transactional(propagation = Propagation.REQUIRES_NEW)
	public void invalidateAll(IUser user) {
		repository.findAllByUser(user)
			.forEach(rt -> service.invalidate(rt.id()));
	}
}
