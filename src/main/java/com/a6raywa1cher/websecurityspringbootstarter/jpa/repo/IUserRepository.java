package com.a6raywa1cher.websecurityspringbootstarter.jpa.repo;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IUserRepository<T extends AbstractUser> extends CrudRepository<T, Long> {
    Optional<T> findByUsername(String username);
}
