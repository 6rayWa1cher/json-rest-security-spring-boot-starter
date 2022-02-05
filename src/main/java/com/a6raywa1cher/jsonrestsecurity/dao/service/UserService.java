package com.a6raywa1cher.jsonrestsecurity.dao.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

import java.util.Optional;

/**
 * Provides methods to find and update users
 *
 * @see DefaultUserService
 */
public interface UserService {
	/**
	 * Updates last visit timestamp to the current time.
	 *
	 * @param user the input user
	 */
	void updateLastVisitAt(IUser user);

	/**
	 * Retracts a user by id
	 *
	 * @param id user id
	 * @return user or empty optional
	 */
	Optional<IUser> getById(Long id);

	/**
	 * Retracts a user by login
	 *
	 * @param login user login
	 * @return user or empty optional
	 */
	Optional<IUser> getByLogin(String login);

	/**
	 * Retracts a user by login and raw password
	 *
	 * @param login       user login
	 * @param rawPassword user raw password
	 * @return user or empty optional
	 */
	Optional<IUser> getByLoginAndPassword(String login, String rawPassword);
}
