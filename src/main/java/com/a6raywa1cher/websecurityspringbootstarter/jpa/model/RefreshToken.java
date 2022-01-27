package com.a6raywa1cher.websecurityspringbootstarter.jpa.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RefreshToken {
    private Long id;

    private String token;

    private LocalDateTime expiringAt;
}
