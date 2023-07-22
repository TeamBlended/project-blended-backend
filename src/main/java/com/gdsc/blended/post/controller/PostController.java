package com.gdsc.blended.post.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.GeoListResponseDto;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.dto.PostUpdateRequestDto;
import com.gdsc.blended.post.service.PostService;
import com.gdsc.blended.utils.ApiResponse;
import com.gdsc.blended.utils.PagingResponse;
import com.gdsc.blended.utils.PagingUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;


    //개시글 쓰기
    @PostMapping(value = "/posts/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostResponseDto>> createPost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long categoryId, @AuthenticationPrincipal UserInfo user) throws IOException {

        PostResponseDto createdPost = postService.createPost(postRequestDto, categoryId, postRequestDto.getMultipartFile(), user.getEmail());
        ApiResponse<PostResponseDto> response = ApiResponse.success(createdPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "마감 시간이 되면 자동으로 마감 처리")
    @GetMapping("/posts")
    public ResponseEntity<ApiResponse<PagingResponse<PostResponseDto>>> getAllPost(@RequestParam(defaultValue = "0") int page,
                                                                      @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postService.getAllPost(pageable);
        PagingResponse<PostResponseDto> pagingResponse = PagingUtil.toResponse(postPage);
        ApiResponse<PagingResponse<PostResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> updatePost(@PathVariable Long postId, @RequestBody PostUpdateRequestDto postUpdateRequestDto, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto updatedPost = postService.updatePost(postId,postUpdateRequestDto , user.getEmail());
        ApiResponse<PostResponseDto> response = ApiResponse.success(updatedPost);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //게시글 삭제
    // TODO: 2023/07/22 삭제 방식 바꿔야됨
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        postService.deletePost(postId, postRequestDto.getMultipartFile(), user.getEmail());
        return ResponseEntity.noContent().build();
    }

    //게시글 상세 구현
    //조회수 구현
    // TODO: 2023/07/22 이거 형식좀 일관성있게 바꿔야될듯
    @GetMapping("/posts/detail/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> detailPost(@PathVariable Long postId ,@AuthenticationPrincipal UserInfo user){
        PostResponseDto postResponseDto = postService.detailPost(postId, user.getEmail());
        if (postResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        ApiResponse<PostResponseDto> response = ApiResponse.success(postResponseDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "게시글 모집 마감 on/off 버튼")
    @PutMapping("/posts/{postId}/complete")
    public ResponseEntity<ApiResponse<PostResponseDto>> completePost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto completedPost = postService.completePost(postId, user.getEmail());
        ApiResponse<PostResponseDto> response = ApiResponse.success(completedPost);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //검색
    @GetMapping("/posts/{keyword}")
    public ResponseEntity<ApiResponse<PagingResponse<PostResponseDto>>> searchPosts(@PathVariable String keyword){
        Page<PostResponseDto> postResponseDtoList = postService.searchPosts(keyword);
        PagingResponse<PostResponseDto>pagingResponse = PagingUtil.toResponse(postResponseDtoList);
        ApiResponse<PagingResponse<PostResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

}
