package com.gdsc.blended.comment.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.service.CommentService;
import com.gdsc.blended.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @Operation(summary = "댓글작성")
    @PostMapping()
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long postId , @AuthenticationPrincipal UserEntity user) {
        CommentResponseDto responseDto = commentService.createComment(requestDto,postId, user.getId());
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "댓글조회(필요없을듯)")
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> getcomment(@PathVariable Long commentId){
        CommentResponseDto comment = commentService.getComments(commentId);
        return ResponseEntity.ok(comment);
    }
    @Operation(summary = "해당게시글에 속한 댓글 리스트")
    @GetMapping()
    public ResponseEntity<List<CommentResponseDto>> getCommentListByPost(@PathVariable Long postId) {
        List<CommentResponseDto> comments = commentService.getCommentListByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @Operation(summary = "댓글 수정")
    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserEntity user){
        CommentResponseDto updateComment = commentService.updateComment(requestDto, commentId, user.getId());
        return ResponseEntity.ok(updateComment);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{commentId}/delete")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserEntity user){
        CommentResponseDto deleteComment = commentService.deleteComment(commentId,user.getId());
        return ResponseEntity.ok(deleteComment);
    }

    @Operation(summary = "댓글 DB까지 삭제")
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long commentId, @AuthenticationPrincipal UserEntity user){
        commentService.realDeleteComment(commentId, user.getId());
        return ResponseEntity.noContent().build();
    }

}

