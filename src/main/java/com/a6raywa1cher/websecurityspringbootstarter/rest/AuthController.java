package com.a6raywa1cher.websecurityspringbootstarter.rest;

import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.dao.model.IUser;
import com.a6raywa1cher.websecurityspringbootstarter.dao.model.RefreshToken;
import com.a6raywa1cher.websecurityspringbootstarter.dao.service.UserService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.JwtRefreshPair;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.JwtRefreshPairService;
import com.a6raywa1cher.websecurityspringbootstarter.jwt.service.RefreshTokenService;
import com.a6raywa1cher.websecurityspringbootstarter.rest.req.GetNewJwtTokenRequest;
import com.a6raywa1cher.websecurityspringbootstarter.rest.req.InvalidateTokenRequest;
import com.a6raywa1cher.websecurityspringbootstarter.rest.req.LoginRequest;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Optional;

@RequestMapping("/auth")
public class AuthController {
	private final RefreshTokenService refreshTokenService;

	private final AuthenticationResolver authenticationResolver;

	private final JwtRefreshPairService jwtRefreshPairService;

	private final UserService userService;

	public AuthController(AuthenticationResolver authenticationResolver, RefreshTokenService refreshTokenService,
						  JwtRefreshPairService jwtRefreshPairService, UserService userService) {
		this.authenticationResolver = authenticationResolver;
		this.refreshTokenService = refreshTokenService;
		this.jwtRefreshPairService = jwtRefreshPairService;
		this.userService = userService;
	}

	@GetMapping("/check")
	public ResponseEntity<?> check() {
		return ResponseEntity.ok().build();
	}

	@PostMapping("/login")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> convertToJwt(@RequestBody @Valid LoginRequest loginRequest,
													   HttpServletRequest request) {
		Optional<IUser> optional = userService.getByLoginAndPassword(loginRequest.getUsername(), loginRequest.getPassword());
		if (optional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		IUser user = optional.get();
		JwtRefreshPair pair = jwtRefreshPairService.issue(user);
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(pair);
	}

	@PostMapping("/get_access")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> getNewJwtToken(@RequestBody @Valid GetNewJwtTokenRequest request) {
		Optional<IUser> user = userService.getById(request.getUserId());
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
		Optional<IUser> user = userService.getById(request.getUserId());
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
