package com.a6raywa1cher.jsonrestsecurity.dao.model;

import java.time.LocalDateTime;
import java.util.List;

/**
 * General facade for user-specific User classes.
 * <br/>
 * Contains necessary getters and setters for required fields.
 * Using Lombok's annotations {@code @Getter @Setter} with corresponding fields
 * should be enough.
 *
 * @see AbstractUser
 */
public interface IUser {
	Long getId();

	Object getUserRole();

	LocalDateTime getLastVisitAt();

	void setLastVisitAt(LocalDateTime lastVisit);

	String getPassword();

	List<RefreshToken> getRefreshTokens();

	void setRefreshTokens(List<RefreshToken> refreshTokens);
}
