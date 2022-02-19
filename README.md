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

Compatible with springdoc-openapi.

## Installation

Add the dependency to your pom.xml

```xml

<dependency>
   <groupId>com.a6raywa1cher</groupId>
   <artifactId>json-rest-security-spring-boot-starter</artifactId>
   <version>0.0.1-SNAPSHOT</version>
</dependency>
```

## Docs

### Creating user and user repository

You can create a user class in two different ways:

1. Extend the `AbstractUser` class. Therefore, all necessary field will be presented without any additional code.
2. Implement the `IUser` interface. You could create these fields in your user class to comply the interface with
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

`RefreshToken` isn't an Entity in terms of JPA. By default, they're stored as JSON array field in the user class. You
can, however, extend class and change it.

In most cases you find it useful to implement `UserService` or extend `DefaultUserService`.

### Access-Refresh authorization flow

1. User sends request to `POST /auth/login` with body:
    ```json
    {
      "username": "abcdef",
      "password": "qwerty"
    }
    ```
   If the username/password pair is valid, server will return a pair of tokens:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "refreshTokenExpiringAt": "2022-03-05T12:42:44.212708+03:00",
      "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwicnRpIjoiYjU0YjkxNjgtYTMwNC00YTYwLTlkMDctZWNjNzRmYzkzOTM5IiwiaXNzIjoidGVzdC1hcHAiLCJleHAiOjE2NDUyNjQwNjR9.vTU5g7K0j2s6ywD0CAevuMNblKnrmXIUckrIUQ5_LH-Xc8rdc1aD4A-Mvbapqg1r0LuiCV4SEOUw-2HKETX2MQ",
      "accessTokenExpiringAt": "2022-02-19T12:47:44.220801+03:00",
      "userId": 1
    }
    ```
2. With each request, append header with the access token:
    ```
    Authorization: Bearer eyJ0eXAiO...
    ```
3. User can check if accessToken is still works with `GET /auth/check`.
4. If the accessToken is expired, request new one with the refreshToken at `POST /auth/get_access` with body:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "userId": 1
    }
    ```
   User will get another pair of tokens.
4. User can request `DELETE /auth/invalidate` to invalidate refresh token and associated access token with body:
    ```json
    {
      "refreshToken": "8ce451bb-44cb-4fbd-9e9d-02ee3335e176",
      "userId": 1
    }
    ```
5. If user want to make a full logout from all devices, `DELETE /auth/invalidate_all`.

### Granted authorities and user enabled services

Granted authorities of the user are provided by `grantedAuthorityService` bean.  
Default implementation returns two types of authorities based on `SimpleGrantedAuthority`:

- `ROLE_${userRole}`. For example: `ROLE_USER`.
- `ENABLED`, if user is enabled.

The user is enabled if `userEnabledChecker` bean returns true. By default, it's always true.

You are free to implement your own `grantedAuthorityService` and `userEnabledChecker` beans by overriding
`GrantedAuthorityService` and `UserEnabledChecker`.

### Fail limiter

Fail-limiter is a submodule of json-rest-security. It prevents password bruteforce attacks.  
The client will be banned for `block-duration` after `max-attempts` fails. Fail attempt count is reset after
`block-duration` since last failed request.  
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

Any feature requests, improvements, bug and security reports are welcome!
