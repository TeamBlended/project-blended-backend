package com.gdsc.blended.post.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.GeoListResponseDto;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.service.PostService;
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
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;


    //개시글 쓰기
    @PostMapping(value = "/posts/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostResponseDto> createPost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long categoryId, @AuthenticationPrincipal UserInfo user) throws IOException {

        PostResponseDto createdPost = postService.createPost(postRequestDto, categoryId, postRequestDto.getMultipartFile(), user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    @Operation(summary = "마감 시간이 되면 자동으로 마감 처리")
    @GetMapping("/posts")
    public ResponseEntity<Page<PostResponseDto>> getAllPost(@RequestParam(defaultValue = "0") int page,
                                                            @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<PostResponseDto> postPage = postService.getAllPost(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(postPage);
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserInfo user) {
        PostResponseDto updatedPost = postService.updatePost(postId, postRequestDto, user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(updatedPost);
    }

    //게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        postService.deletePost(postId, postRequestDto.getMultipartFile(), user.getEmail());
        return ResponseEntity.noContent().build();
    }

    //게시글 상세 구현
    //조회수 구현
    @GetMapping("/posts/detail/{postId}")
    public ResponseEntity<PostResponseDto> detailPost(@PathVariable Long postId ,@AuthenticationPrincipal UserInfo user){
        PostResponseDto postResponseDto = postService.detailPost(postId, user.getEmail());
        if (postResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(postResponseDto);
    }
    @Operation(summary = "게시글 모집 마감 on/off 버튼")
    @PutMapping("/posts/{postId}/complete")
    public ResponseEntity<PostResponseDto> completePost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto completedPost = postService.completePost(postId, user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(completedPost);
    }

    //검색
    @GetMapping("/posts/{keyword}")
    public ResponseEntity<List<PostResponseDto>> searchPosts(@PathVariable String keyword){
        List<PostResponseDto> postResponseDtoList = postService.searchPosts(keyword);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseDtoList);
    }

}
