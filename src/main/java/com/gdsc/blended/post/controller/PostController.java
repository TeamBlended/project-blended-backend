package com.gdsc.blended.post.controller;

import com.gdsc.blended.common.image.dto.ImageDto;
import com.gdsc.blended.common.image.service.ImageService;
import com.gdsc.blended.common.image.service.S3UploadService;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.post.dto.request.PostRequestDto;
import com.gdsc.blended.post.dto.response.PostCreateResponseDto;
import com.gdsc.blended.post.dto.response.PostDetailResponseDto;
import com.gdsc.blended.post.dto.response.PostResponseDto;
import com.gdsc.blended.post.dto.request.PostUpdateRequestDto;
import com.gdsc.blended.post.dto.response.SearchResponseDto;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@AllArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class PostController {
    private final PostService postService;
    private final S3UploadService s3UploadService;
    private final ImageService imageService;


    //개시글 쓰기
    @PostMapping(value = "/posts/{categoryId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<PostCreateResponseDto>> createPost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long categoryId, @AuthenticationPrincipal UserInfo user) {
        PostCreateResponseDto createdPost = postService.createPost(postRequestDto, categoryId, user.getEmail());
        ApiResponse<PostCreateResponseDto> response = ApiResponse.success(createdPost);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // 이미지 업로드
    @PostMapping(value = "/uploadImage", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ImageDto>> uploadImage(@RequestParam("file") MultipartFile multipartFile) throws IOException {
            // ImageService를 이용하여 이미지 업로드 처리
            String imageName = s3UploadService.upload(multipartFile, "post");
            imageService.createImage(imageName);
            ImageDto imageDto = new ImageDto();
            imageDto.setPath(imageName);

            ApiResponse<ImageDto> response = ApiResponse.success(imageDto);
            return ResponseEntity.ok(response);
    }

    @Operation(summary = "게시글 목록 전체 조회")
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
    @Operation(summary = "게시글 DB 삭제(사용안함, 게발자용)")
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<Void> deletePost(@ModelAttribute PostRequestDto postRequestDto, @PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        postService.deletePostDB(postId, user.getEmail());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/posts/delete/{postId}")
    public ResponseEntity<ApiResponse<PostResponseDto>> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto deletedPost = postService.deletePost(postId, user.getEmail());
        ApiResponse<PostResponseDto> response = ApiResponse.success(deletedPost);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //게시글 상세 구현
    //조회수 구현
    @GetMapping("/posts/{postId}")
    public ResponseEntity<ApiResponse<PostDetailResponseDto>> detailPost(@PathVariable Long postId , @AuthenticationPrincipal UserInfo user){
        PostDetailResponseDto postResponseDto = postService.detailPost(postId, user.getEmail());
        if (postResponseDto == null) {
            return ResponseEntity.notFound().build();
        }
        ApiResponse<PostDetailResponseDto> response = ApiResponse.success(postResponseDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "게시글 모집 마감 on/off 버튼(사용 x)")
    @PutMapping("/posts/{postId}/complete")
    public ResponseEntity<ApiResponse<PostResponseDto>> completePost(@PathVariable Long postId, @AuthenticationPrincipal UserInfo user) {
        PostResponseDto completedPost = postService.completePost(postId, user.getEmail());
        ApiResponse<PostResponseDto> response = ApiResponse.success(completedPost);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    //검색
    @GetMapping("/posts/search")
    public ResponseEntity<ApiResponse<PagingResponse<SearchResponseDto>>> searchPosts(@RequestParam String keyword, @RequestParam int page, @RequestParam int size){
        Pageable pageable = PageRequest.of(page, size);
        Page<SearchResponseDto> postResponseDtoList = postService.searchPosts(keyword , pageable);
        PagingResponse<SearchResponseDto>pagingResponse = PagingUtil.toResponse(postResponseDtoList);
        ApiResponse<PagingResponse<SearchResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }



}
