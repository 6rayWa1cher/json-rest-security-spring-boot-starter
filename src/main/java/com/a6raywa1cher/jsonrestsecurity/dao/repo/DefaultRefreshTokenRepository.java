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
@SuppressWarnings({"ClassCanBeRecord", "unchecked"})
public class DefaultRefreshTokenRepository<T extends IUser> implements RefreshTokenRepository {
	private final IUserRepository<T> userRepository;

	public DefaultRefreshTokenRepository(IUserRepository<T> userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public List<RefreshToken> findAllByUser(IUser user) {
		return user.getRefreshTokens();
	}

	@Override
	public void deleteAllFromUser(IUser user, List<RefreshToken> listToDelete) {
		List<String> idsToDelete = listToDelete.stream()
			.map(RefreshToken::getId)
			.toList();
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> idsToDelete.contains(rt.getId()));
		user.setRefreshTokens(refreshTokens);
		userRepository.save((T) user);
	}

	@Override
	public RefreshToken save(IUser user, RefreshToken refreshToken) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> rt.getId().equals(refreshToken.getId()));
		refreshTokens.add(refreshToken);
		user.setRefreshTokens(refreshTokens);
		userRepository.save((T) user);
		return refreshToken;
	}

	@Override
	public Optional<RefreshToken> findByToken(IUser user, String token) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		return refreshTokens.stream()
			.filter(rt -> rt.getToken().equals(token))
			.findFirst();
	}

	@Override
	public void delete(IUser user, RefreshToken refreshToken) {
		List<RefreshToken> refreshTokens = new ArrayList<>(user.getRefreshTokens());
		refreshTokens.removeIf(rt -> rt.getId().equals(refreshToken.getId()));
		user.setRefreshTokens(refreshTokens);
		userRepository.save((T) user);
	}
}
