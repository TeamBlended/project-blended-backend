package com.gdsc.blended.post.controller;

import com.gdsc.blended.post.dto.GeoListResponseDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<GeoListResponseDto>> getPostsByDistance(
            @RequestParam("nowLatitude") Double latitude,
            @RequestParam("nowLongitude") Double longitude
    ) {
        List<GeoListResponseDto> posts = postService.getPostsByDistance(latitude, longitude );
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @Operation(summary = "최신 순으로 게시글 가져오기")
    @GetMapping("/posts/newestList")
    public ResponseEntity<Page<PostResponseDto>> getNewestPosts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<PostResponseDto> posts = postService.getNewestPosts(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(posts);
    }

    @Operation(summary = "좋아요 많은 순으로 게시글 가져오기")
    @GetMapping("/posts/heartList")
    public ResponseEntity<Page<PostResponseDto>> heartList(@RequestParam(defaultValue = "0") int page,
                                                           @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postService.getPostsSortedByHeart(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(postPage);
    }
}
