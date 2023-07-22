package com.gdsc.blended.post.heart.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.heart.service.HeartService;
import com.gdsc.blended.utils.ApiResponse;
import com.gdsc.blended.utils.PagingResponse;
import com.gdsc.blended.utils.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
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
    public ResponseEntity<ApiResponse<String>> likeBoard(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        try {
            heartService.likeBoard(postId, user.getEmail());
            ApiResponse<String> apiResponse = new ApiResponse<>("ok", 200, "SUCCESS");
            return ResponseEntity.ok(apiResponse);
        } catch (Exception e) {
            ApiResponse<String> apiResponse = new ApiResponse<>(null, 500, "Failed to like the post");
            return ResponseEntity.status(500).body(apiResponse);
        }
    }

    @Transactional
    @GetMapping("/myPage/heartList")
    public ResponseEntity<ApiResponse<PagingResponse<PostResponseDto>>> myHeartList(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserInfo user
    ) {
        Page<PostResponseDto> postPage = heartService.getMyHeartList(page, size, user.getEmail());
        PagingResponse<PostResponseDto> pagingResponse = PagingUtil.toResponse(postPage);
        ApiResponse<PagingResponse<PostResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}