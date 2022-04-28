package com.a6raywa1cher.jsonrestsecurity.rest;

import com.a6raywa1cher.jsonrestsecurity.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.jsonrestsecurity.dao.model.IUser;
import com.a6raywa1cher.jsonrestsecurity.dao.model.RefreshToken;
import com.a6raywa1cher.jsonrestsecurity.dao.service.IUserService;
import com.a6raywa1cher.jsonrestsecurity.jwt.JwtRefreshPair;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.jsonrestsecurity.jwt.service.RefreshTokenService;
import com.a6raywa1cher.jsonrestsecurity.rest.req.GetNewJwtTokenRequest;
import com.a6raywa1cher.jsonrestsecurity.rest.req.InvalidateTokenRequest;
import com.a6raywa1cher.jsonrestsecurity.rest.req.LoginRequest;
import com.a6raywa1cher.jsonrestsecurity.rest.res.UserInfo;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/auth")
@ResponseBody
@SuppressWarnings("ClassCanBeRecord")
public class AuthController {
	private final RefreshTokenService refreshTokenService;

	private final AuthenticationResolver authenticationResolver;

	private final JwtRefreshPairService jwtRefreshPairService;

	private final IUserService<?> iUserService;

	public AuthController(AuthenticationResolver authenticationResolver, RefreshTokenService refreshTokenService,
						  JwtRefreshPairService jwtRefreshPairService, IUserService<?> iUserService) {
		this.authenticationResolver = authenticationResolver;
		this.refreshTokenService = refreshTokenService;
		this.jwtRefreshPairService = jwtRefreshPairService;
		this.iUserService = iUserService;
	}

	@GetMapping("/check")
	public ResponseEntity<?> check() {
		return ResponseEntity.ok().build();
	}

	@GetMapping("/user")
	public ResponseEntity<UserInfo> getSelf() {
		IUser user = authenticationResolver.getUser();
		return ResponseEntity.ok(new UserInfo(user.getId(), user.getUsername(), user.getUserRole() == null ? null : user.getUserRole().toString()));
	}

	@PostMapping("/login")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> convertToJwt(@RequestBody @Valid LoginRequest loginRequest,
													   HttpServletRequest request) {
		Optional<?> optional = iUserService.getByLoginAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
		if (optional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		IUser user = (IUser) optional.get();
		JwtRefreshPair pair = jwtRefreshPairService.issue(user);
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(pair);
	}

	@PostMapping("/get_access")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> getNewJwtToken(@RequestBody @Valid GetNewJwtTokenRequest request) {
		Optional<IUser> user = iUserService.getById(request.getUserId()).map(u -> (IUser) u);
		Optional<RefreshToken> optional = user.flatMap(
			u -> refreshTokenService.getByToken(u, request.getRefreshToken())
		);
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		refreshTokenService.invalidate(user.get(), optional.get());
		return ResponseEntity.ok(jwtRefreshPairService.issue(user.get()));
	}

	@DeleteMapping("/invalidate")
	public ResponseEntity<Void> invalidateToken(@RequestBody @Valid InvalidateTokenRequest request) {
		Optional<IUser> user = iUserService.getById(request.getUserId()).map(u -> (IUser) u);
		Optional<RefreshToken> optional = user.flatMap(u -> refreshTokenService.getByToken(u, request.getRefreshToken()));
		optional.ifPresent(refreshToken -> refreshTokenService.invalidate(user.get(), refreshToken));
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/invalidate_all")
	public ResponseEntity<Void> invalidateAllTokens() {
		IUser user = authenticationResolver.getUser();
		refreshTokenService.invalidateAll(user);
		return ResponseEntity.ok().build();
	}
}
