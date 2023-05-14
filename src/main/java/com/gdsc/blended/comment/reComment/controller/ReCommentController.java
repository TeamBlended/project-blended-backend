package com.gdsc.blended.comment.reComment.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.reComment.dto.ReCommentRequestDto;
import com.gdsc.blended.comment.reComment.dto.ReCommentResponseDto;
import com.gdsc.blended.comment.reComment.service.ReCommentService;
import com.gdsc.blended.comment.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post/{postId}/comment/{commentId}/reComment")
public class ReCommentController {
    private final ReCommentService reCommentService;

    public ReCommentController(ReCommentService reCommentService) {
        this.reCommentService = reCommentService;
    }

    @PostMapping()
    public ResponseEntity<ReCommentResponseDto> createReComment(@RequestBody ReCommentRequestDto requestDto, @PathVariable Long commentId) {
        ReCommentResponseDto responseDto = reCommentService.createReComment(requestDto,commentId);
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "대댓글조회(필요없을듯)")
    @GetMapping("/{reCommentId}")
    public ResponseEntity<ReCommentResponseDto> getReComment(@PathVariable Long reCommentId){
        ReCommentResponseDto reComment = reCommentService.getReComments(reCommentId);
        return ResponseEntity.ok(reComment);
    }
    @Operation(summary = "해당뎃글에 속한 대댓글 리스트")
    @GetMapping()
    public ResponseEntity<List<ReCommentResponseDto>> getReCommentListByPost(@PathVariable Long commentId) {
        List<ReCommentResponseDto> reComments = reCommentService.getReCommentListByPost(commentId);
        return ResponseEntity.ok(reComments);
    }

    @PutMapping("/{reCommentId}")
    public ResponseEntity<ReCommentResponseDto> updateReComment(@RequestBody CommentRequestDto requestDto, @PathVariable Long reCommentId){
        ReCommentResponseDto updateComment = reCommentService.updateReComment(requestDto, reCommentId);
        return ResponseEntity.ok(updateComment);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "대댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{reCommentId}/delete")
    public ResponseEntity<ReCommentResponseDto> deleteComment(@PathVariable Long reCommentId){
        ReCommentResponseDto deleteComment = reCommentService.deleteComment(reCommentId);
        return ResponseEntity.ok(deleteComment);
    }

    @DeleteMapping("/{reCommentId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long reCommentId){
        reCommentService.realDeleteComment(reCommentId);
        return ResponseEntity.noContent().build();
    }

}