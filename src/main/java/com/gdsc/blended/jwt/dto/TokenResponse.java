package com.gdsc.blended.jwt.dto;

public record TokenResponse(
        String grantType,
        String accessToken,
        String refreshToken,
        Long accessTokenExpiresIn
) { }
