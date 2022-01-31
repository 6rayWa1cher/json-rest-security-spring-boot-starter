package com.a6raywa1cher.websecurityspringbootstarter.dao.repo;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public interface IUserRepository<T extends IUser> extends CrudRepository<T, Long> {
    Optional<T> findByUsername(String username);
}
