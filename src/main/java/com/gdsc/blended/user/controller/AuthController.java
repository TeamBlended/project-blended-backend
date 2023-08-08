package com.gdsc.blended.user.controller;

import com.gdsc.blended.jwt.dto.LogoutRequest;
import com.gdsc.blended.jwt.dto.SocialLoginRequest;
import com.gdsc.blended.jwt.dto.TokenResponse;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.service.AuthService;
import com.gdsc.blended.common.message.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/google")
    public ResponseEntity<ApiResponse<TokenResponse>> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) throws Exception {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest.token());
        ApiResponse<TokenResponse> response = ApiResponse.success(tokenResponse);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        TokenResponse tokenResponse = authService.reissue(user.getEmail(),user.getName(), logoutRequest.refreshToken());
        ApiResponse<TokenResponse> response = ApiResponse.success(tokenResponse);
        return ResponseEntity.ok(response);
    }

}
