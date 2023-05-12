package com.gdsc.blended.comment.controller;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.service.CommentService;
import com.gdsc.blended.user.entity.User;
import net.minidev.json.annotate.JsonIgnore;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/post/comment")
    public ResponseEntity<CommentResponseDto> createComment(@RequestBody CommentRequestDto requestDto, @AuthenticationPrincipal User loginUser ) {
        CommentResponseDto responseDto = commentService.createComment(requestDto, loginUser);
        return ResponseEntity.ok(responseDto);
    }

}

