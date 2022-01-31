package com.a6raywa1cher.websecurityspringbootstarter.dao.model;

import java.time.LocalDateTime;

public record RefreshToken(String id, String token, LocalDateTime expiringAt) {

}
