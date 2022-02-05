package com.a6raywa1cher.jsonrestsecurity.dao.repo;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

import java.util.Optional;

/**
 * Provides methods to operate User objects
 * <br/>
 * Complies with Spring Data JPA {@link org.springframework.data.jpa.repository.JpaRepository} notation
 *
 * @param <T> exact implementation of {@link IUser} interface
 * @see DefaultUserRepository
 */
public interface IUserRepository<T extends IUser> {
	Optional<T> findByUsername(String username);

	<S extends T> S save(S s);

	Optional<T> findById(Long id);
}
