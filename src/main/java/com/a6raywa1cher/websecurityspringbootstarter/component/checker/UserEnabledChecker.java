package com.a6raywa1cher.websecurityspringbootstarter.component.checker;

import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;

public interface UserEnabledChecker {
    boolean check(AbstractUser user);
}
