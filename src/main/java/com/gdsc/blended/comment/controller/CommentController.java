package com.gdsc.blended.comment.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.service.CommentService;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.common.message.ApiResponse;
import com.gdsc.blended.common.utils.PagingResponse;
import com.gdsc.blended.common.utils.PagingUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글작성")
    @PostMapping()
    public ResponseEntity<ApiResponse<CommentResponseDto>> createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postId , @AuthenticationPrincipal UserInfo user) {
        CommentResponseDto responseDto = commentService.createComment(requestDto,postId, user.getEmail());
        ApiResponse<CommentResponseDto> response = ApiResponse.success(responseDto);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글조회(필요없을듯)")
    @GetMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> getcomment(@PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        CommentResponseDto comment = commentService.getComments(commentId, user.getEmail());
        ApiResponse<CommentResponseDto> response = ApiResponse.success(comment);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "해당게시글에 속한 댓글 리스트")
    @GetMapping()
    public ResponseEntity<ApiResponse<PagingResponse<CommentResponseDto>>> getCommentListByPost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size, @PathVariable Long postId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<CommentResponseDto> comments = commentService.getCommentListByPost(postId, pageable);
        PagingResponse<CommentResponseDto> pagingResponse = PagingUtil.toResponse(comments);
        ApiResponse<PagingResponse<CommentResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<ApiResponse<CommentResponseDto>> updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        CommentResponseDto updateComment = commentService.updateComment(requestDto, commentId, user.getEmail());
        ApiResponse<CommentResponseDto> response = ApiResponse.success(updateComment);
        return ResponseEntity.ok(response);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{commentId}/delete")
    public ResponseEntity<ApiResponse<CommentResponseDto>> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        CommentResponseDto deleteComment = commentService.deleteComment(commentId,user.getEmail());
        ApiResponse<CommentResponseDto> response = ApiResponse.success(deleteComment);
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "댓글 DB까지 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserInfo user){
        commentService.realDeleteComment(commentId, user.getEmail());
        return ResponseEntity.noContent().build();
    }

}

