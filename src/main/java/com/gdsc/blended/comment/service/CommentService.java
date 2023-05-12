package com.gdsc.blended.comment.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
import com.gdsc.blended.user.entity.User;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentResponseDto createComment(CommentRequestDto requestDto,@AuthenticationPrincipal User loginUser){
        PostEntity post = postRepository.findById(requestDto.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("해당 POST를 찾을 수 없습니다."));

        CommentEntity comment = CommentEntity.builder()
                .content(requestDto.getContent())
                .post(post)
                .user(loginUser)
                .build();
        CommentEntity savedComment = (CommentEntity) commentRepository.save(comment);

        // Comment 엔티티 저장 로직

        return CommentResponseDto.builder()
                .id(savedComment.getCommentId())
                .content(savedComment.getContent())
                .postId(savedComment.getPost().getId())
                .build();
    }
}
