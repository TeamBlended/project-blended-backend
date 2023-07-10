package com.gdsc.blended.post.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.GeoListResponseDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.service.PostService;
import com.gdsc.blended.utils.PagingResponse;
import com.gdsc.blended.utils.PagingUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostListController {
    private PostService postService;

    @Operation(summary = "가까운 순으로 게시글 목록 가져오기")
    @GetMapping("/posts/distanceList")
    public ResponseEntity<PagingResponse<GeoListResponseDto>> getPostsByDistance(
            @RequestParam("nowLatitude") Double latitude,
            @RequestParam("nowLongitude") Double longitude
    ) {
        Page<GeoListResponseDto> posts = postService.getPostsByDistance(latitude, longitude);
        PagingResponse<GeoListResponseDto>response = PagingUtil.toResponse(posts);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "최신 순으로 게시글 가져오기")
    @GetMapping("/posts/newestList")
    public ResponseEntity<PagingResponse<PostResponseDto>> getNewestPosts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<PostResponseDto> posts = postService.getNewestPosts(page, size);
        PagingResponse<PostResponseDto> response = PagingUtil.toResponse(posts);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "좋아요 많은 순으로 게시글 가져오기")
    @GetMapping("/posts/heartList")
    public ResponseEntity<PagingResponse<PostResponseDto>> heartList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postService.getPostsSortedByHeart(pageable);
        PagingResponse<PostResponseDto> response = PagingUtil.toResponse(postPage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "내가 작성한 게시글 리스트")
    @GetMapping("/posts/myList")
    public ResponseEntity<PagingResponse<PostResponseDto>> myPostList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo userInfo){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postService.getMyPostList(userInfo.getEmail());
        PagingResponse<PostResponseDto> response = PagingUtil.toResponse(postPage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
