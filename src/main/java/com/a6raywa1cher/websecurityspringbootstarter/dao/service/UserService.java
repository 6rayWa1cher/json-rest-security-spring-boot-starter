package com.a6raywa1cher.websecurityspringbootstarter.dao.service;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;

import java.util.Optional;

public interface UserService {
    void updateLastVisitAt(IUser user);

    Optional<IUser> getById(Long id);

    Optional<IUser> getByLogin(String login);

    Optional<IUser> getByLoginAndPassword(String login, String rawPassword);
}
