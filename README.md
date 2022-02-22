# json-rest-security-spring-boot-starter

[![Actions Status](https://github.com/6rayWa1cher/json-rest-security-spring-boot-starter/actions/workflows/tests.yml/badge.svg)](https://github.com/6rayWa1cher/json-rest-security-spring-boot-starter/actions/workflows/tests.yml)
[![codecov](https://codecov.io/gh/6rayWa1cher/json-rest-security-spring-boot-starter/branch/master/graph/badge.svg?token=XTXEC698X8)](https://codecov.io/gh/6rayWa1cher/json-rest-security-spring-boot-starter)  
Basic security enclosure for JSON REST backends on Spring Boot WebMvc

## About

Json-rest-security-spring-boot-starter is a starter for Spring Boot that sets up a simple security enclosure. Including:

- CORS settings
- Basic and Access-Refresh (JWT+UUID) authentications
- Basic AuthController for authentications
- User entity facades
- Fail limiting and last visit filters

## Installation

Add the dependency to your pom.xml

```xml

<dependency>
   <groupId>com.a6raywa1cher</groupId>
   <artifactId>json-rest-security-spring-boot-starter</artifactId>
   <version>0.0.1</version>
</dependency>
```

## Getting started

### No-Setup

If you want to try and test the starter, just start your application and read "Access-Refresh authorization flow".  
However, it isn't a recommended approach.

### Full setup

You can create a user class in two different ways:

1. Extend the `AbstractUser` class. This way, all necessary fields will be presented without any additional code.
2. Implement the `IUser` interface. You could create these fields in your user class to comply with the interface with
   setters and getters:
    ```java
    private Long id;
    
    private String userRole;
    
    private String password;
    
    private List<RefreshToken> refreshTokens;
    
    private LocalDateTime lastVisitAt;
    ```

Then create UserRepository with the `IUserRepository` interface:

```java

@Repository
public interface UserRepository extends IUserRepository<User>, CrudRepository<User, Long> {
}
```

And finally create UserServiceImpl with the `AbstractUserService` superclass:

```java

@Component
public class UserServiceImpl extends AbstractUserService<User> {
   public UserServiceImpl(IUserRepository<User> userRepository, PasswordEncoder passwordEncoder) {
      super(userRepository, passwordEncoder);
   }

   @Override
   public User create(String login, String rawPassword, String role) {
      User user = new User();
      user.setUsername(login);
      user.setPassword(passwordEncoder.encode(rawPassword));
      user.setUserRole(role);
      return userRepository.save(user);
   }
}
```

`RefreshToken` isn't an Entity in terms of JPA. By default, refresh tokens are stored as a JSON array field in the user
class. You can, however, extend the class and change it.

## Docs

### Extending SecurityConfig (WebSecurityConfigurerAdapter)

The simplest way to modify your Spring Security configuration is to extend the `JsonRestWebSecurityConfigurer` class. An
example:

```java

@Configuration
public class SecurityConfig extends JsonRestWebSecurityConfigurer {
   @Override
   protected void configure(HttpSecurity http) throws Exception {
      http.authorizeRequests()
              .antMatchers("/home/admin").hasAnyRole("ADMIN");

      super.configure(http);
   }
}
```

`JsonRestWebSecurityConfigurer` has a couple of options to mention:

1. To prevent the usage of `.anyMatcher()`, use `setUseAnyMatcher(false)`
2. If you need to customize `JsonRestWebSecurityConfigurer` in a special way, you can
   override `void configure(HttpSecurity http)` and
   `void configure(AuthenticationManagerBuilder auth)`. Check out the
   implementation [here](https://github.com/6rayWa1cher/json-rest-security-spring-boot-starter/blob/master/src/main/java/com/a6raywa1cher/jsonrestsecurity/web/JsonRestWebSecurityConfigurer.java)
   .

### Basic authorization flow

Provide a header with the access token with every request:

```
Authorization: Basic YWJjZGVmOnF3ZXJ0eQ==
```

### Access-Refresh authorization flow

1. The User sends a request to `POST /auth/login` with body:
    ```json
    {
      "username": "abcdef",
      "password": "qwerty"
    }
    ```
   If the username/password pair is valid, the server will return a pair of tokens:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "refreshTokenExpiringAt": "2022-03-05T12:42:44.212708+03:00",
      "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicnRpIjoiYjU0YjkxNjgtYTMwNC00YTYwLTlkMDctZWNjNzRmYzkzOTM5IiwiaXNzIjoidGVzdC1hcHAiLCJleHAiOjE2NDUyNjQwNjR9.vTU5g7K0j2s6ywD0CAevuMNblKnrmXIUckrIUQ5_LH-Xc8rdc1aD4A-Mvbapqg1r0LuiCV4SEOUw-2HKETX2MQ",
      "accessTokenExpiringAt": "2022-02-19T12:47:44.220801+03:00",
      "userId": 1
    }
    ```
2. Provide a header with the access token with every request:
    ```
    Authorization: Bearer eyJ0eXAiO...
    ```
3. The user can check if accessToken is still works with `GET /auth/check`.
4. If the accessToken is expired, request a new one with the refreshToken at `POST /auth/get_access` with body:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "userId": 1
    }
    ```
   The user will get a new pair of tokens.
4. The user can request `DELETE /auth/invalidate` to invalidate refresh token and associated access token with body:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "userId": 1
    }
    ```
5. If user want to make a full logout from all devices, use `DELETE /auth/invalidate_all`.

AuthController is compatible with springdoc-openapi.

### Granted authorities and users enable check services

Granted authorities of the user are provided by `grantedAuthorityService` bean.  
The default implementation returns two types of authorities based on `SimpleGrantedAuthority`:

- `ROLE_${userRole}`. For example, `ROLE_USER`.
- `ENABLED` if the user is enabled.

The user is enabled if the `userEnabledChecker` bean returns true. By default, it's always true.

You are free to implement your `grantedAuthorityService` and `userEnabledChecker` beans by overriding
`GrantedAuthorityService` and `UserEnabledChecker`.

### Fail limiter

Fail-limiter is a submodule of json-rest-security. It prevents password brute-force attacks.  
The client will be banned for `block-duration` after `max-attempts` fails. Failure attempt count is reset after
`block-duration` since the last failed request.  
The request is failed in terms of fail-limiter if the response code is 401 (Unauthorized) or 403 (Forbidden).  
Properties for fail-limiter are listed below.

### Properties

#### Required properties

| Property name                   | Description          | Default value               |
|---------------------------------|----------------------|-----------------------------|
| `json-rest-security.jwt.secret` | JWT token secret key | Generated at each app start |

#### Optional properties

| Property name                                        | Description                                     | Default value      |
|------------------------------------------------------|-------------------------------------------------|--------------------|
| `json-rest-security.enable`                          | Enable or disable `json-rest-security`          | true               |
| `json-rest-security.jwt.access-duration`             | Access token duration                           | PT5M               |
| `json-rest-security.jwt.refresh-duration`            | Refresh token duration                          | P14D               |
| `json-rest-security.jwt.max-refresh-tokens-per-user` | Max refresh token count per user                | 10                 |
| `json-rest-security.jwt.issuer-name`                 | Issuer name (presented in JWT tokens)           | json-rest-security |
| `json-rest-security.fail-limiter.enable`             | Enable or disable fail-limiter module           | true               |
| `json-rest-security.fail-limiter.block-duration`     | Client block time after `max-attempts` fails    | PT1M               |
| `json-rest-security.fail-limiter.max-attempts`       | Max attempts to fail before ban on fail-limiter | 5                  |
| `json-rest-security.fail-limiter.max-cache-size`     | Max block list size                             | 30000              |
| `json-rest-security.enable-auth-controller`          | Enable or disable AuthController                | true               |
| `json-rest-security.enable-default-web-config`       | Enable or disable JsonRestWebSecurityConfigurer | true               |

## Contribution

Any feature requests, improvements, bug reports, and security reports are welcome!
