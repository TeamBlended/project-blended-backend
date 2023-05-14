package com.gdsc.blended.comment.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/post/{postId}/comment")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping()
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto,@PathVariable Long postId) {
        CommentResponseDto responseDto = commentService.createComment(requestDto,postId);
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

    @PutMapping("/{commentId}")
    public ResponseEntity<CommentResponseDto> updateComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long commentId){
        CommentResponseDto updateComment = commentService.updateComment(requestDto, commentId);
        return ResponseEntity.ok(updateComment);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{commentId}/delete")
    public ResponseEntity<CommentResponseDto> deleteComment(@PathVariable Long commentId){
        CommentResponseDto deleteComment = commentService.deleteComment(commentId);
        return ResponseEntity.ok(deleteComment);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long commentId){
        commentService.realDeleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

}

