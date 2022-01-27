package com.a6raywa1cher.websecurityspringbootstarter.jwt;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class JwtToken {
    private String token;

    private LocalDateTime expiringAt;

    private long uid;

    private long refreshId;
}
