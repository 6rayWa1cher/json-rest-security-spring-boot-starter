package com.a6raywa1cher.jsonrestsecurity.dao.repo;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;

import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository {
	List<RefreshToken> findAllByUser(IUser user);

	void deleteAllFromUser(IUser user, List<RefreshToken> listToDelete);

	RefreshToken save(IUser user, RefreshToken refreshToken);

	Optional<RefreshToken> findByToken(IUser user, String token);

	void delete(IUser user, RefreshToken refreshToken);
}
