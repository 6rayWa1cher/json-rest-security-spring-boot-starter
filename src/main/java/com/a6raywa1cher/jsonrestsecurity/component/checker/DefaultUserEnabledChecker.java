package com.a6raywa1cher.jsonrestsecurity.component.checker;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

public class DefaultUserEnabledChecker implements UserEnabledChecker {
    @Override
    public boolean check(IUser user) {
        return true;
    }
}
