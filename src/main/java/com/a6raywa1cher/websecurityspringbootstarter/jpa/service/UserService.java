package com.a6raywa1cher.websecurityspringbootstarter.jpa.service;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;

import java.util.Optional;

public interface UserService {
    void updateLastVisitAt(AbstractUser user);

    Optional<AbstractUser> getById(Long id);

    Optional<AbstractUser> getByLogin(String login);

    Optional<AbstractUser> getByLoginAndPassword(String login, String rawPassword);
}
