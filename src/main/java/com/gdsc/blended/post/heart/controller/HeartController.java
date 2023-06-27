package com.gdsc.blended.post.heart.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.heart.service.HeartService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    @GetMapping("/myPage/heartList")
    public ResponseEntity<Page<PostResponseDto>> myHeartList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserInfo user
    ) {
        Page<PostResponseDto> postPage = heartService.getMyHeartList(page, size, user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(postPage);
    }
}