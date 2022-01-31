package com.a6raywa1cher.websecurityspringbootstarter.component.checker;

import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;

public class DefaultUserEnabledChecker implements UserEnabledChecker {
    @Override
    public boolean check(IUser user) {
        return true;
    }
}
