package com.gdsc.blended.jwt.dto;

import org.springframework.http.HttpStatus;

public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) {

}
