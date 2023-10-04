package com.gdsc.blended.user.controller;


import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.dto.response.AuthorNicknameDto;
import com.gdsc.blended.user.dto.response.UserMeDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.service.UserService;
import com.gdsc.blended.common.message.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService userService;

    @PutMapping("/{userId}/nickname")
    public ResponseEntity<ApiResponse<AuthorNicknameDto>> updateUserNickname(@AuthenticationPrincipal UserInfo user, @RequestBody String newNickname){
            UserEntity updatedUser = userService.updateUserNickname(user.getEmail(), newNickname);
            AuthorNicknameDto authorNicknameDto = new AuthorNicknameDto(updatedUser.getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(authorNicknameDto));
    }

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserMeDto>> getMyProfile(@AuthenticationPrincipal UserInfo user) {
        UserEntity userEntity;
        try {
            userEntity = userService.getUserByEmail(user.getEmail());
        } catch (AuthenticationException e) {
            ApiResponse<UserMeDto> apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
        }
        UserMeDto userMeDto = new UserMeDto(userEntity.getId(), userEntity.getNickname(), userEntity.getProfileImageUrl());
        ApiResponse<UserMeDto> apiResponse = ApiResponse.success(userMeDto);
        return ResponseEntity.status(HttpStatus.OK).body(apiResponse);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiResponse<String>> handleAuthenticationException(AuthenticationException exception) {
        ApiResponse<String> apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiResponse);
    }

    /*@GetMapping("/me")
    public ResponseEntity<ApiResponse<UserMeDto>> getMyProfile(@AuthenticationPrincipal UserInfo user, String accesToken){
        UserEntity userEntity = userService.getUserByEmail(user.getEmail(), accesToken);
        UserMeDto userMeDto = new UserMeDto( userEntity.getId(),userEntity.getNickname(), userEntity.getProfileImageUrl());
        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(userMeDto));
    }*/


}

