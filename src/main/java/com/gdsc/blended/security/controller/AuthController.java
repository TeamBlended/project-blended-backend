package com.gdsc.blended.security.controller;

import com.gdsc.blended.security.dto.TokenRequestDto;
import com.gdsc.blended.security.dto.UserRequestDto;
import com.gdsc.blended.security.service.AuthService;
import com.gdsc.blended.user.dto.UserPostDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequiredArgsConstructor
@Transactional
public class AuthController {
    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity login(@RequestBody UserRequestDto userRequestDto) {
        return new ResponseEntity<>(authService.login(userRequestDto), HttpStatus.OK);
    }

    @PostMapping("/reissue")
    public ResponseEntity reissue(@RequestBody TokenRequestDto tokenRequestDto) {
        return new ResponseEntity<>(authService.reissue(tokenRequestDto), HttpStatus.OK);
    }
}
