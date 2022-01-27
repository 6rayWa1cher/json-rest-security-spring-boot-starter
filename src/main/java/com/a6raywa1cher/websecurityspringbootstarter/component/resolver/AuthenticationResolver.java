package com.a6raywa1cher.websecurityspringbootstarter.component.resolver;


import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public interface AuthenticationResolver {
    AbstractUser getUser() throws AuthenticationException;

    AbstractUser getUser(Authentication authentication) throws AuthenticationException;
}
