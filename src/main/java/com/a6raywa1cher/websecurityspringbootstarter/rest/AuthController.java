package com.a6raywa1cher.websecurityspringbootstarter.rest;

import com.a6raywa1cher.websecurityspringbootstarter.component.resolver.AuthenticationResolver;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.AbstractUser;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.model.RefreshToken;
import com.a6raywa1cher.websecurityspringbootstarter.jpa.service.UserService;
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

@RestController
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

	@PostMapping("/login")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> convertToJwt(@RequestBody @Valid LoginRequest loginRequest,
													   HttpServletRequest request) {
		Optional<AbstractUser> optional = userService.getByLoginAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
		if (optional.isEmpty()) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}
		AbstractUser user = optional.get();
		JwtRefreshPair pair = jwtRefreshPairService.issue(user);
		SecurityContextHolder.clearContext();
		request.getSession().invalidate();
		return ResponseEntity.ok(pair);
	}

	@PostMapping("/get_access")
	@SecurityRequirements // erase jwt login
	public ResponseEntity<JwtRefreshPair> getNewJwtToken(@RequestBody @Valid GetNewJwtTokenRequest request) {
		AbstractUser user = authenticationResolver.getUser();
		Optional<RefreshToken> optional = refreshTokenService.getByToken(user, request.getRefreshToken());
		if (optional.isEmpty()) {
			return ResponseEntity.notFound().build();
		}
		refreshTokenService.invalidate(user, optional.get());
		return ResponseEntity.ok(jwtRefreshPairService.issue(user));
	}

	@DeleteMapping("/invalidate")
	public ResponseEntity<Void> invalidateToken(@RequestBody @Valid InvalidateTokenRequest request) {
		AbstractUser user = authenticationResolver.getUser();
		Optional<RefreshToken> optional = refreshTokenService.getByToken(user, request.getRefreshToken());
		optional.ifPresent(refreshToken -> refreshTokenService.invalidate(user, refreshToken));
		return ResponseEntity.ok().build();
	}

	@DeleteMapping("/invalidate_all")
	public ResponseEntity<Void> invalidateAllTokens() {
		AbstractUser user = authenticationResolver.getUser();
		refreshTokenService.invalidateAll(user);
		return ResponseEntity.ok().build();
	}
}
