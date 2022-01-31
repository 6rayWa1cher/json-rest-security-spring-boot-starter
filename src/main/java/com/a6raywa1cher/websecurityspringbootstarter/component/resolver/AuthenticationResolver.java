package com.a6raywa1cher.websecurityspringbootstarter.component.resolver;


import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationResolver {
    IUser getUser() throws AuthenticationException;

    IUser getUser(Authentication authentication) throws AuthenticationException;
}
