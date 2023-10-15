package com.gdsc.blended.post.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.response.GeoListResponseDto;
import com.gdsc.blended.post.dto.response.PostListResponseDto;
import com.gdsc.blended.post.dto.response.PostResponseDto;
import com.gdsc.blended.post.service.PostService;
import com.gdsc.blended.common.message.ApiResponse;
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


@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostListController {
    private PostService postService;

    @Operation(summary = "가까운 순으로 게시글 목록 가져오기")
    @GetMapping("/posts/distanceList")
    public ResponseEntity<ApiResponse<PagingResponse<GeoListResponseDto>>> getPostsByDistance(
            @RequestParam("nowLatitude") Double latitude,
            @RequestParam("nowLongitude") Double longitude,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<GeoListResponseDto> posts = postService.getPostsByDistance(latitude, longitude, page, size);
        PagingResponse<GeoListResponseDto>pagingResponse = PagingUtil.toResponse(posts);
        ApiResponse<PagingResponse<GeoListResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "최신 순으로 게시글 가져오기")
    @GetMapping("/posts/newestList")
    public ResponseEntity<ApiResponse<PagingResponse<PostListResponseDto>>> getNewestPosts(
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size
    ) {
        Page<PostListResponseDto> posts = postService.getNewestPosts(page, size);
        PagingResponse<PostListResponseDto> pagingResponse = PagingUtil.toResponse(posts);
        ApiResponse<PagingResponse<PostListResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "좋아요 많은 순으로 게시글 가져오기")
    @GetMapping("/posts/heartList")
    public ResponseEntity<ApiResponse<PagingResponse<PostListResponseDto>>> heartList(@RequestParam(defaultValue = "0") int page,
                                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListResponseDto> postPage = postService.getPostsSortedByHeart(pageable);
        PagingResponse<PostListResponseDto> pagingResponse = PagingUtil.toResponse(postPage);
        ApiResponse<PagingResponse<PostListResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Operation(summary = "내가 작성한 게시글 리스트")
    @GetMapping("/posts/myList")
    public ResponseEntity<ApiResponse<PagingResponse<PostListResponseDto>>> myPostList(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @AuthenticationPrincipal UserInfo userInfo){
        Pageable pageable = PageRequest.of(page, size);
        Page<PostListResponseDto> postPage = postService.getMyPostList(userInfo.getEmail());
        PagingResponse<PostListResponseDto> pagingResponse = PagingUtil.toResponse(postPage);
        ApiResponse<PagingResponse<PostListResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}
