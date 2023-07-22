package com.gdsc.blended.user.controller;


import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.service.UserService;
import com.gdsc.blended.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<ApiResponse<UserEntity>> updateUserNickname(@AuthenticationPrincipal UserInfo user, @RequestParam("newNickname") String newNickname){
        try {
            UserEntity updatedUser = userService.updateUserNickname(user.getEmail(), newNickname);
            ApiResponse<UserEntity> apiResponse = new ApiResponse<>(updatedUser, 200, "SUCCESS");
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<UserEntity> apiResponse = new ApiResponse<>(null, 500, "Failed to update user nickname");
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

}

