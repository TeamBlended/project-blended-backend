package com.gdsc.blended.post.controller;

import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.PostRequestDto;
import com.gdsc.blended.post.dto.PostResponseDto;
import com.gdsc.blended.post.service.PostService;
import com.gdsc.blended.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
public class PostController {
    private PostService postService;

    //개시글 쓰기
    @PostMapping("/posts/{categoryId}")
    public ResponseEntity<PostResponseDto> createPost(@RequestBody PostRequestDto postRequestDto, @PathVariable Long categoryId, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto createdPost = postService.createPost(postRequestDto, categoryId, user.getEmail());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPost);
    }

    //모든 게시글 가져오기
    @GetMapping("/posts")
    public ResponseEntity<List<PostResponseDto>> getAllPost(){
        List<PostResponseDto> postList =postService.getAllPost();
        return ResponseEntity.status(HttpStatus.OK).body(postList);
    }

    //게시글 수정
    @PutMapping("/posts/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable Long postId, @RequestBody PostRequestDto postRequestDto,@AuthenticationPrincipal UserInfo user) {
        PostResponseDto updatedPost = postService.updatePost(postId, postRequestDto, user.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(updatedPost);
    }

    //게시긓 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        postService.deletePost(postId,user.getEmail());
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

    //TODO .. 찜수 구현

    //TODO .. 게

    //TODO .. 게시글 상세

}
