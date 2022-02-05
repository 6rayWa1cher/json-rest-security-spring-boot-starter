package com.a6raywa1cher.jsonrestsecurity.component.checker;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

/**
 * Tests wherever {@link IUser} is permitted to make any authorized requests.
 * <br/>
 * It could be useful for email validation checks. In this case, {@link #check} could
 * return {@code false} if the user hasn't validated the email.
 * <br/>
 *
 * @see DefaultUserEnabledChecker
 */
public interface UserEnabledChecker {
    boolean check(IUser user);
}
