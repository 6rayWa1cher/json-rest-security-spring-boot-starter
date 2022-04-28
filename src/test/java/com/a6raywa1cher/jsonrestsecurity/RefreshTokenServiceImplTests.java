package com.a6raywa1cher.jsonrestsecurity;

import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;
import com.a6raywa1cher.jsonrestsecurity.dao.repo.RefreshTokenRepository;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.BlockedRefreshTokensService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.impl.RefreshTokenServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.LongStream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RefreshTokenServiceImplTests {
	final long MAX_TOKENS_PER_USER = 5;
	final Duration REFRESH_TOKEN_DURATION = Duration.ofSeconds(30);
	@Mock
	RefreshTokenRepository repository;
	@Mock
	BlockedRefreshTokensService blockedService;

	@Test
	void issue__overloadCheck__equals() {
		RefreshTokenService service = new RefreshTokenServiceImpl(repository, blockedService, MAX_TOKENS_PER_USER, REFRESH_TOKEN_DURATION);
		IUser user = mock(IUser.class);

		List<RefreshToken> refreshTokens = LongStream.range(0, MAX_TOKENS_PER_USER)
			.mapToObj(i -> RefreshToken.uuidToken(LocalDateTime.now().plusSeconds(15 * (i + 1))))
			.toList();

		when(repository.findAllByUser(user)).thenReturn(refreshTokens);
		when(repository.save(eq(user), any())).thenAnswer(inv -> inv.getArguments()[1]);

		RefreshToken issue = service.issue(user);

		verify(repository).save(user, issue);
		verify(repository).deleteAllFromUser(user, List.of(refreshTokens.get(0)));
		verify(blockedService).invalidate(user.getId(), refreshTokens.get(0).getId());
	}

	@Test
	void issue__overloadCheck__less() {
		RefreshTokenService service = new RefreshTokenServiceImpl(repository, blockedService, MAX_TOKENS_PER_USER, REFRESH_TOKEN_DURATION);
		IUser user = mock(IUser.class);

		List<RefreshToken> refreshTokens = LongStream.range(0, MAX_TOKENS_PER_USER - 1)
			.mapToObj(i -> RefreshToken.uuidToken(LocalDateTime.now().plusSeconds(15 * (i + 1))))
			.toList();

		when(repository.findAllByUser(user)).thenReturn(refreshTokens);
		when(repository.save(eq(user), any())).thenAnswer(inv -> inv.getArguments()[1]);

		RefreshToken issue = service.issue(user);

		verify(repository).save(user, issue);
		verify(repository, never()).deleteAllFromUser(eq(user), any());
		verify(blockedService, never()).invalidate(any(), any());
	}

	@Test
	void issue__overloadCheck__greater() {
		RefreshTokenService service = new RefreshTokenServiceImpl(repository, blockedService, MAX_TOKENS_PER_USER, REFRESH_TOKEN_DURATION);
		IUser user = mock(IUser.class);

		List<RefreshToken> refreshTokens = LongStream.range(0, MAX_TOKENS_PER_USER + 2)
			.mapToObj(i -> RefreshToken.uuidToken(LocalDateTime.now().plusSeconds(15 * (i + 1))))
			.toList();

		when(repository.findAllByUser(user)).thenReturn(refreshTokens);
		when(repository.save(eq(user), any())).thenAnswer(inv -> inv.getArguments()[1]);

		RefreshToken issue = service.issue(user);

		verify(repository).save(user, issue);
		verify(repository).deleteAllFromUser(user, List.of(refreshTokens.get(0), refreshTokens.get(1), refreshTokens.get(2)));
		verify(blockedService).invalidate(user.getId(), refreshTokens.get(0).getId());
		verify(blockedService).invalidate(user.getId(), refreshTokens.get(1).getId());
		verify(blockedService).invalidate(user.getId(), refreshTokens.get(2).getId());
	}
}
