package com.a6raywa1cher.jsonrestsecurity.dao.model;

import java.time.LocalDateTime;
import java.util.List;

public interface IUser {
	Long getId();

	Object getUserRole();

	LocalDateTime getLastVisitAt();

	void setLastVisitAt(LocalDateTime lastVisit);

	String getPassword();

	List<RefreshToken> getRefreshTokens();

	void setRefreshTokens(List<RefreshToken> refreshTokens);
}
