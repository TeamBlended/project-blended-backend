package com.gdsc.blended.comment.replies.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.replies.dto.RepliesRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesResponseDto;
import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import com.gdsc.blended.comment.replies.repository.RepliesRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@AllArgsConstructor
@Service
public class RepliesService {
    private final RepliesRepository repliesRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    public RepliesEntity findById(Long id){
        return repliesRepository.findById(id).orElseThrow(()-> new IllegalArgumentException("Invalid Comment ID"));
    }

    public RepliesResponseDto createReplies(RepliesRequestDto requestDto, Long commentId, String email){
        CommentEntity comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글을 찾을 수 없습니다."));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));

        RepliesEntity replies = RepliesEntity.builder()
                .content(requestDto.getContent())
                .comment(comment)
                .user(user)
                .build();
        RepliesEntity savedReplies = repliesRepository.save(replies);

        // Comment 엔티티 저장 로직

        return RepliesResponseDto.builder()
                .repliesId(savedReplies.getId())
                .content(savedReplies.getContent())
                .user(savedReplies.getUser())
                .modifiedDate(savedReplies.getModifiedDate())
                .build();
    }

    public RepliesResponseDto getReplies(Long reCommentId) {
        RepliesEntity comment = repliesRepository.findById(reCommentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + reCommentId));

        return RepliesResponseDto.builder()
                .repliesId(comment.getId())
                .content(comment.getContent())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    public List<RepliesResponseDto> getRepliesListByPost(Long postId) {
        List<RepliesEntity> comments = repliesRepository.findByCommentId(postId);
        List<RepliesResponseDto> repliesResponseDtos = new ArrayList<>();

        for (RepliesEntity comment : comments) {
            RepliesResponseDto repliesResponseDto = RepliesResponseDto.builder()
                    .repliesId(comment.getId())
                    .content(comment.getContent())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
            repliesResponseDtos.add(repliesResponseDto);
        }

        return repliesResponseDtos;
    }

    public RepliesResponseDto updateReplies(CommentRequestDto requestDto, Long reCommentId, String email) {
        RepliesEntity replies = repliesRepository.findById(reCommentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + reCommentId));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if(!replies.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }
        {
            replies.updateContent(requestDto.getContent());

            RepliesEntity updateReplies= repliesRepository.save(replies);

            return RepliesResponseDto.builder()
                    .repliesId(updateReplies.getId())
                    .content(updateReplies.getContent())
                    .modifiedDate(updateReplies.getModifiedDate())
                    .user(user)
                    .build();
        }
    }

    public RepliesResponseDto deleteReplies(Long repliesId, String email) {
        RepliesEntity replies = repliesRepository.findById(repliesId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " +repliesId));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if(!replies.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }
        {
            replies.deleteReComment(replies.getContent());

            RepliesEntity updatedComment = repliesRepository.save(replies);

            return RepliesResponseDto.builder()
                    .repliesId(updatedComment.getId())
                    .content(updatedComment.getContent())
                    .modifiedDate(updatedComment.getModifiedDate())
                    .build();
        }
    }

    public void realDeleteReplies(Long repliesId, String email) {
        RepliesEntity replies = repliesRepository.findById(repliesId)
                .orElseThrow(() -> new IllegalArgumentException("Incalid ReComment id"));
        UserEntity user = userRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("유저가 정보가 없습니다."));
        if(!replies.getUser().equals(user)){
            throw new IllegalArgumentException("해당 댓글을 작성한 유저가 아닙니다.");
        }
        {
            repliesRepository.delete(replies);
        }
    }
}