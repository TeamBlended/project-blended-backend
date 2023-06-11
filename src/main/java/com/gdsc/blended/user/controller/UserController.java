package com.gdsc.blended.user.controller;


import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.dto.UserRequestDto;
import com.gdsc.blended.user.dto.response.UserResponseDto;
import com.gdsc.blended.user.repository.UserRepository;
import com.gdsc.blended.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    @PutMapping("/user/{userId}")
    public ResponseEntity<UserResponseDto> updateNickname(@PathVariable("userId") Long userId, @RequestBody UserRequestDto userRequestDto, @AuthenticationPrincipal UserInfo user) {
        String userEmail = user.getEmail();
        UserResponseDto updateddUser = userService.updateNickname(userRequestDto, userEmail);
        return ResponseEntity.ok(updateddUser);
    }
}
