package com.a6raywa1cher.websecurityspringbootstarter.component.authority;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public interface GrantedAuthorityService {
    Collection<GrantedAuthority> getAuthorities(IUser user);
}
