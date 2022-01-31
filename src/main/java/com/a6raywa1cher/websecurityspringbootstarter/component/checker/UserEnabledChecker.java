package com.a6raywa1cher.websecurityspringbootstarter.component.checker;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;

public interface UserEnabledChecker {
    boolean check(IUser user);
}
