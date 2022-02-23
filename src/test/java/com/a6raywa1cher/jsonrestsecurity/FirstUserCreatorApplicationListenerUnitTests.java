package com.a6raywa1cher.jsonrestsecurity;

import com.a6raywa1cher.jsonrestsecurity.component.FirstUserCreatorApplicationListener;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.context.event.ApplicationReadyEvent;

import java.util.Optional;

import static com.a6raywa1cher.jsonrestsecurity.utils.StringUtils.randomString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FirstUserCreatorApplicationListenerUnitTests {
	final String USERNAME = randomString(10);
	final String PASSWORD = randomString(10);
	final String ROLE = randomString(10);
	@Mock
	IUserService<IUser> IUserService;

	@Test
	void onApplicationEvent__userNotExists() {
		FirstUserCreatorApplicationListener listener = new FirstUserCreatorApplicationListener(USERNAME, PASSWORD, ROLE, IUserService);

		when(IUserService.getByLogin(USERNAME)).thenReturn(Optional.empty());

		listener.onApplicationEvent(mock(ApplicationReadyEvent.class));

		verify(IUserService).getByLogin(USERNAME);
		verify(IUserService).create(USERNAME, PASSWORD, ROLE);
	}

	@Test
	void onApplicationEvent__userExists() {
		FirstUserCreatorApplicationListener listener = new FirstUserCreatorApplicationListener(USERNAME, PASSWORD, ROLE, IUserService);

		when(IUserService.getByLogin(USERNAME)).thenReturn(Optional.of(mock(IUser.class)));

		listener.onApplicationEvent(mock(ApplicationReadyEvent.class));

		verify(IUserService).getByLogin(USERNAME);
		verify(IUserService, never()).create(USERNAME, PASSWORD, ROLE);
	}
}
