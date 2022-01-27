package com.a6raywa1cher.websecurityspringbootstarter.jwt.service;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtRefreshPair;

public interface JwtRefreshPairService {
    JwtRefreshPair issue(AbstractUser user);
}
