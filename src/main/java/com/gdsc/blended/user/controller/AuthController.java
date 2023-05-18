package com.gdsc.blended.user.controller;

import com.gdsc.blended.jwt.dto.LogoutRequest;
import com.gdsc.blended.jwt.dto.SocialLoginRequest;
import com.gdsc.blended.jwt.dto.TokenResponse;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) throws GeneralSecurityException, IOException {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest.token());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        TokenResponse tokenResponse = authService.reissue(user.getEmail(),user.getNickname(), logoutRequest.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        authService.logout(user.getEmail() , logoutRequest.refreshToken());
        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }
}
