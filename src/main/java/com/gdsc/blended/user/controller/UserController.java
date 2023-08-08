package com.gdsc.blended.user.controller;


import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.dto.response.AuthorNicknameDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.service.UserService;
import com.gdsc.blended.common.message.ApiResponse;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<ApiResponse<AuthorNicknameDto>> updateUserNickname(@AuthenticationPrincipal UserInfo user, @RequestParam("newNickname") String newNickname){
            UserEntity updatedUser = userService.updateUserNickname(user.getEmail(), newNickname);
            AuthorNicknameDto authorNicknameDto = new AuthorNicknameDto(updatedUser.getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(authorNicknameDto));

    }

}

