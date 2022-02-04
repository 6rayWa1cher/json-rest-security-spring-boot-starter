package com.a6raywa1cher.jsonrestsecurity.jwt.service;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtRefreshPair;

public interface JwtRefreshPairService {
	JwtRefreshPair issue(IUser user);
}
