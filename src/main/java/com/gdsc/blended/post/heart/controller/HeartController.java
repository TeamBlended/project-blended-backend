package com.gdsc.blended.post.heart.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class HeartController {

    private final HeartService heartService;

    @PostMapping("/posts/{postId}/heart")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> likeBoard(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        heartService.likeBoard(postId, user.getEmail());
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}