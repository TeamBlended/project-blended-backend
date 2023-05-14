package com.gdsc.blended.comment.reComment.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.reComment.dto.ReCommentRequestDto;
import com.gdsc.blended.comment.reComment.dto.ReCommentResponseDto;
import com.gdsc.blended.comment.reComment.entity.ReCommentEntity;
import com.gdsc.blended.comment.reComment.repository.ReCommentRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class ReCommentService {
    private final ReCommentRepository reCommentRepository;
    private final CommentRepository commentRepository;

    public ReCommentEntity findById(Long id){
        return reCommentRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid Comment ID"));
    }
    public ReCommentResponseDto createReComment(ReCommentRequestDto requestDto, Long commentId){
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));

        ReCommentEntity reComment = ReCommentEntity.builder()
                .content(requestDto.getContent())
                .comment(comment)
                .build();
        ReCommentEntity savedReComment = reCommentRepository.save(reComment);

        // Comment 엔티티 저장 로직

        return ReCommentResponseDto.builder()
                .reCommentId(savedReComment.getId())
                .content(savedReComment.getContent())
                .modifiedDate(savedReComment.getModifiedDate())
                .build();
    }

    public ReCommentResponseDto getReComments(Long reCommentId) {
        ReCommentEntity comment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + reCommentId));

        return ReCommentResponseDto.builder()
                .reCommentId(comment.getId())
                .content(comment.getContent())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    public List<ReCommentResponseDto> getReCommentListByPost(Long postId) {
        List<ReCommentEntity> comments = reCommentRepository.findByCommentId(postId);
        List<ReCommentResponseDto> reCommentResponseDtos = new ArrayList<>();

        for (ReCommentEntity comment : comments) {
            ReCommentResponseDto reCommentResponseDto = ReCommentResponseDto.builder()
                    .reCommentId(comment.getId())
                    .content(comment.getContent())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
            reCommentResponseDtos.add(reCommentResponseDto);
        }

        return reCommentResponseDtos;
    }

    public ReCommentResponseDto updateReComment(CommentRequestDto requestDto, Long reCommentId) {
        ReCommentEntity reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + reCommentId));

        reComment.updateContent(requestDto.getContent());

        ReCommentEntity updatedReComment = reCommentRepository.save(reComment);

        return ReCommentResponseDto.builder()
                .reCommentId(updatedReComment.getId())
                .content(updatedReComment.getContent())
                .modifiedDate(updatedReComment.getModifiedDate())
                .build();
    }

    public ReCommentResponseDto deleteComment(Long commentId) {
        ReCommentEntity comment = reCommentRepository.findById(commentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + commentId));

        comment.deletReComment(comment.getContent());

        ReCommentEntity updatedComment = reCommentRepository.save(comment);

        return ReCommentResponseDto.builder()
                .reCommentId(updatedComment.getId())
                .content(updatedComment.getContent())
                .modifiedDate(updatedComment.getModifiedDate())
                .build();
    }

    public void realDeleteComment(Long reCommentId) {
        ReCommentEntity reComment = reCommentRepository.findById(reCommentId)
                .orElseThrow(() -> new IllegalArgumentException("Incalid ReComment id"));
        reCommentRepository.delete(reComment);
    }
}