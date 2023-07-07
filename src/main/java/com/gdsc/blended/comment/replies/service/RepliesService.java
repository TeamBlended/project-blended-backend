package com.gdsc.blended.comment.replies.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.replies.dto.RepliesRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesResponseDto;
import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import com.gdsc.blended.comment.replies.repository.RepliesRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.user.dto.response.AuthorDto;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @Transactional
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
        AuthorDto authorDto = AuthorDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return RepliesResponseDto.builder()
                .repliesId(savedReplies.getId())
                .content(savedReplies.getContent())
                .user(authorDto)
                .modifiedDate(savedReplies.getModifiedDate())
                .build();
    }

    @Transactional
    public RepliesResponseDto getReplies(Long reCommentId) {
        RepliesEntity comment = repliesRepository.findById(reCommentId)
                .orElseThrow(() -> new NoSuchElementException("Comment not found with id: " + reCommentId));

        return RepliesResponseDto.builder()
                .repliesId(comment.getId())
                .content(comment.getContent())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    @Transactional
    public Page<RepliesResponseDto> getRepliesListByPost(Long postId, Pageable pageable) {
        List<RepliesEntity> comments = repliesRepository.findByCommentId(postId);
        List<RepliesResponseDto> repliesResponseDtos = new ArrayList<>();

        for (RepliesEntity comment : comments) {
            RepliesResponseDto repliesResponseDto = RepliesResponseDto.builder()
                    .repliesId(comment.getId())
                    .content(comment.getContent())
                    .user(AuthorDto.builder()
                            .nickname(comment.getUser().getNickname())
                            .profileImageUrl(comment.getUser().getProfileImageUrl())
                            .build())
                    .modifiedDate(comment.getModifiedDate())
                    .build();
            repliesResponseDtos.add(repliesResponseDto);
        }

        return new PageImpl<>(repliesResponseDtos);
    }

    @Transactional
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

            AuthorDto authorDto = AuthorDto.builder()
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();

            return RepliesResponseDto.builder()
                    .repliesId(updateReplies.getId())
                    .content(updateReplies.getContent())
                    .user(authorDto)
                    .modifiedDate(updateReplies.getModifiedDate())
                    .build();
        }
    }

    @Transactional
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

    @Transactional
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