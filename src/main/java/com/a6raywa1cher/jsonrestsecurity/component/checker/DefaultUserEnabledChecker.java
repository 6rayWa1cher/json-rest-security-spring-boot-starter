package com.a6raywa1cher.jsonrestsecurity.component.checker;

import com.a6raywa1cher.jsonrestsecurity.component.SecurityComponentsConfiguration;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;

/**
 * The default implementation of {@link DefaultUserEnabledChecker}.
 * <br/>
 * Always returns true.
 * <br/>
 *
 * @see DefaultUserEnabledChecker
 * @see SecurityComponentsConfiguration
 */
public class DefaultUserEnabledChecker implements UserEnabledChecker {
	@Override
	public boolean check(IUser user) {
		return true;
	}
}
