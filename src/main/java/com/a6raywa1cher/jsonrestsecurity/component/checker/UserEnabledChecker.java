package com.a6raywa1cher.jsonrestsecurity.component.checker;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

public interface UserEnabledChecker {
	boolean check(IUser user);
}
