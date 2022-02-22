package com.a6raywa1cher.jsonrestsecurity.dao.repo;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Provides methods to operate User objects. Extends the {@link CrudRepository} interface.
 *
 * @param <T> exact implementation of {@link IUser} interface
 * @see DefaultUserRepository
 */
public interface IUserRepository<T extends IUser> extends CrudRepository<T, Long> {
	Optional<T> findByUsername(String username);

	<S extends T> S save(S s);

	Optional<T> findById(Long id);
}
