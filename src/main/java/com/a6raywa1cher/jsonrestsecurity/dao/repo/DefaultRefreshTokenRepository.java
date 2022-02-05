package com.a6raywa1cher.jsonrestsecurity.dao.repo;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * The default implementation of {@link RefreshTokenRepository}.
 * <br/>
 * Stores {@link RefreshToken} in IUser.
 *
 * @see com.a6raywa1cher.jsonrestsecurity.dao.DaoConfiguration
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class DefaultRefreshTokenRepository implements RefreshTokenRepository {
	private final IUserRepository userRepository;

	public DefaultRefreshTokenRepository(IUserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<RefreshToken> findAllByUser(IUser user) {
		return user.getRefreshTokens();
	}

	@Override
	public void deleteAllFromUser(IUser user, List<RefreshToken> listToDelete) {
		List<String> idsToDelete = listToDelete.stream()
			.map(RefreshToken::id)
			.toList();
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> idsToDelete.contains(rt.id()));
		user.setRefreshTokens(refreshTokens);
		userRepository.save(user);
	}

	@Override
	public RefreshToken save(IUser user, RefreshToken refreshToken) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> rt.id().equals(refreshToken.id()));
		refreshTokens.add(refreshToken);
		user.setRefreshTokens(refreshTokens);
		userRepository.save(user);
		return refreshToken;
	}

	@Override
	public Optional<RefreshToken> findByToken(IUser user, String token) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		return refreshTokens.stream()
			.filter(rt -> rt.token().equals(token))
			.findFirst();
	}

	@Override
	public void delete(IUser user, RefreshToken refreshToken) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> rt.id().equals(refreshToken.id()));
		user.setRefreshTokens(refreshTokens);
		userRepository.save(user);
	}
}
