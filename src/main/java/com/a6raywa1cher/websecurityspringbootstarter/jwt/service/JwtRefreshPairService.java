package com.a6raywa1cher.websecurityspringbootstarter.jwt.service;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtRefreshPair;

public interface JwtRefreshPairService {
    JwtRefreshPair issue(IUser user);
}
