package com.gdsc.blended.comment.replies.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesResponseDto;
import com.gdsc.blended.comment.replies.service.RepliesService;
import com.gdsc.blended.jwt.oauth.UserInfo;
import com.gdsc.blended.utils.ApiResponse;
import com.gdsc.blended.utils.PagingResponse;
import com.gdsc.blended.utils.PagingUtil;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/post/{postId}/comment/{commentId}/replies")
public class RepliesController {
    private final RepliesService repliesService;

    public RepliesController(RepliesService repliesService) {
        this.repliesService = repliesService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<RepliesResponseDto>> createReplies(@RequestBody RepliesRequestDto requestDto, @PathVariable Long commentId, @AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto responseDto = repliesService.createReplies(requestDto,commentId,user.getEmail());
        ApiResponse<RepliesResponseDto> response = ApiResponse.success(responseDto);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "대댓글조회(필요없을듯)")
    @GetMapping("/{repliesId}")
    public ResponseEntity<ApiResponse<RepliesResponseDto>> getReplies(@PathVariable Long repliesId){
        RepliesResponseDto replies = repliesService.getReplies(repliesId);
        ApiResponse<RepliesResponseDto> response = ApiResponse.success(replies);
        return ResponseEntity.ok(response);
    }
    @Operation(summary = "해당뎃글에 속한 대댓글 리스트")
    @GetMapping()
    public ResponseEntity<ApiResponse<PagingResponse<RepliesResponseDto>>> getRepliesListByPost(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size,@PathVariable Long commentId) {
        Pageable pageable = PageRequest.of(page, size);
        Page<RepliesResponseDto> replies = repliesService.getRepliesListByPost(commentId,pageable);
        PagingResponse<RepliesResponseDto> pagingResponse = PagingUtil.toResponse(replies);
        ApiResponse<PagingResponse<RepliesResponseDto>> response = ApiResponse.success(pagingResponse);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{repliesId}")
    public ResponseEntity<ApiResponse<RepliesResponseDto>> updateReplies(@RequestBody CommentRequestDto requestDto, @PathVariable Long repliesId, @AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto updateComment = repliesService.updateReplies(requestDto, repliesId, user.getEmail());
        ApiResponse<RepliesResponseDto> response = ApiResponse.success(updateComment);
        return ResponseEntity.ok(response);
    }

    //삭제하기(근데 내용만 안보이게)
    @Operation(summary = "대댓글 삭제하기", description = "실제로 삭제되진 않고 내용만 지워짐")
    @PutMapping("/{repliesId}/delete")
    public ResponseEntity<ApiResponse<RepliesResponseDto>> deleteComment(@PathVariable Long repliesId, @AuthenticationPrincipal UserInfo user) {
        RepliesResponseDto deleteComment = repliesService.deleteReplies(repliesId, user.getEmail());
        ApiResponse<RepliesResponseDto> response = ApiResponse.success(deleteComment);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{repliesId}")
    public ResponseEntity<Void> realDeleteComment(@PathVariable Long repliesId,@AuthenticationPrincipal UserInfo user){
        repliesService.realDeleteReplies(repliesId,user.getEmail());
        return ResponseEntity.noContent().build();
    }

}