package com.gdsc.blended.user.controller;

import com.gdsc.blended.jwt.dto.LogoutRequest;
import com.gdsc.blended.jwt.dto.SocialLoginRequest;
import com.gdsc.blended.jwt.dto.TokenResponse;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.service.AuthService;
import com.gdsc.blended.common.message.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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
        boolean isFirstTimeRegistration = authService.isFirstTimeRegistration(socialLoginRequest.token());
        // 적절한 응답 상태 코드를 설정합니다.
        HttpStatus status = isFirstTimeRegistration ? HttpStatus.CREATED : HttpStatus.OK;

        return ResponseEntity.status(status).body(response);
    }

    @PostMapping("/token")
    public ResponseEntity<ApiResponse<TokenResponse>> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        TokenResponse tokenResponse = authService.reissue(user.getEmail(),user.getName(), logoutRequest.refreshToken());
        ApiResponse<TokenResponse> response = ApiResponse.success(tokenResponse);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/auth/withdraw")
    public ResponseEntity<ApiResponse<String>> withdrawUser(@AuthenticationPrincipal UserInfo user) {
        authService.withdrawUser(user.getEmail());
        ApiResponse<String> response = ApiResponse.success("User has been withdrawn successfully.");
        return ResponseEntity.ok(response);
    }

}
