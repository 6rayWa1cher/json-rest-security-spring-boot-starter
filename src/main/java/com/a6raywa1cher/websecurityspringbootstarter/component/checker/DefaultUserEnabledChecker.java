package com.a6raywa1cher.websecurityspringbootstarter.component.checker;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;

public class DefaultUserEnabledChecker implements UserEnabledChecker {
    @Override
    public boolean check(AbstractUser user) {
        return true;
    }
}
