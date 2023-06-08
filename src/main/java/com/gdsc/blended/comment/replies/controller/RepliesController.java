package com.gdsc.blended.comment.replies.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesResponseDto;
import com.gdsc.blended.comment.replies.service.RepliesService;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.user.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/post/{postId}/comment/{commentId}/replies")
public class RepliesController {
    private final RepliesService repliesService;

    public RepliesController(RepliesService repliesService) {
        this.repliesService = repliesService;
    }

    @PostMapping()
    public ResponseEntity<RepliesResponseDto> createReplies(@RequestBody RepliesRequestDto requestDto, @PathVariable Long commentId,@AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto responseDto = repliesService.createReplies(requestDto,commentId,user.getEmail());
        return ResponseEntity.ok(responseDto);
    }
    @Operation(summary = "대댓글조회(필요없을듯)")
    @GetMapping("/{repliesId}")
    public ResponseEntity<RepliesResponseDto> getReplies(@PathVariable Long repliesId){
        RepliesResponseDto replies = repliesService.getReplies(repliesId);
        return ResponseEntity.ok(replies);
    }
    @Operation(summary = "해당뎃글에 속한 대댓글 리스트")
    @GetMapping()
    public ResponseEntity<List<RepliesResponseDto>> getRepliesListByPost(@PathVariable Long commentId) {
        List<RepliesResponseDto> replies = repliesService.getRepliesListByPost(commentId);
        return ResponseEntity.ok(replies);
    }

    @PutMapping("/{repliesId}")
    public ResponseEntity<RepliesResponseDto> updateReplies(@RequestBody CommentRequestDto requestDto, @PathVariable Long repliesId, @AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto updateComment = repliesService.updateReplies(requestDto, repliesId, user.getEmail());
        return ResponseEntity.ok(updateComment);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "대댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{repliesId}/delete")
    public ResponseEntity<RepliesResponseDto> deleteComment(@PathVariable Long repliesId, @AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto deleteComment = repliesService.deleteReplies(repliesId, user.getEmail());
        return ResponseEntity.ok(deleteComment);
    }

    @DeleteMapping("/{repliesId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long repliesId,@AuthenticationPrincipal UserInfo user){
        repliesService.realDeleteReplies(repliesId,user.getEmail());
        return ResponseEntity.noContent().build();
    }

}