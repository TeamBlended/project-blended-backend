package com.gdsc.blended.user.controller;


import com.gdsc.blended.common.apiResponse.UserResponseMessage;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.service.UserService;
import com.gdsc.blended.common.apiResponse.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.HttpStatus;
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
            return ResponseEntity.ok(new ApiResponse<>(updatedUser, UserResponseMessage.NICKNAME_UPDATE_SUCCESS));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse<>(null, UserResponseMessage.NICKNAME_UPDATE_FAIL));
        }

    }

}

