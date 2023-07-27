package com.gdsc.blended.comment.replies.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.replies.dto.RepliesRequestDto;
import com.gdsc.blended.comment.replies.dto.RepliesResponseDto;
import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import com.gdsc.blended.comment.replies.repository.RepliesRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.common.apiResponse.CommentResponseMessage;
import com.gdsc.blended.common.apiResponse.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
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

@AllArgsConstructor
@Service
public class RepliesService {
    private final RepliesRepository repliesRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;


    public CommentEntity findCommentByCommentId(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND));
    }

    public UserEntity findUserByEmail(String email){
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(UserResponseMessage.USER_NOT_FOUND));
    }

    public RepliesEntity findRepliesByRepliesId(Long repliesId){
        return repliesRepository.findById(repliesId).orElseThrow(()->
                new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND));
    }

    private RepliesEntity checkRepliesOwnerShip(Long repliesId, String email){
        RepliesEntity repliesEntity = findRepliesByRepliesId(repliesId);
        UserEntity user = findUserByEmail(email);
        if (!repliesEntity.getUser().equals(user)) {
            throw new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND);
        }
        return repliesEntity;
    }


    @Transactional
    public RepliesResponseDto createReplies(RepliesRequestDto requestDto, Long commentId, String email){
        CommentEntity comment = findCommentByCommentId(commentId);
        UserEntity user = findUserByEmail(email);

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
    public RepliesResponseDto getReplies(Long repliesId) {
        RepliesEntity comment = findRepliesByRepliesId(repliesId);

        return RepliesResponseDto.builder()
                .repliesId(comment.getId())
                .content(comment.getContent())
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    @Transactional
    public Page<RepliesResponseDto> getRepliesListByComment(Long commentId, Pageable pageable) {
        List<RepliesEntity> comments = repliesRepository.findByCommentId(commentId);
        List<RepliesResponseDto> repliesResponseDtos = new ArrayList<>();

        if (comments.isEmpty())
            throw new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND);
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
    public RepliesResponseDto updateReplies(CommentRequestDto requestDto, Long repliesId, String email) {
        RepliesEntity replies = checkRepliesOwnerShip(repliesId, email);
        UserEntity user = findUserByEmail(email);
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
        RepliesEntity replies = checkRepliesOwnerShip(repliesId, email);

            replies.deleteReComment(replies.getContent());

            RepliesEntity updatedComment = repliesRepository.save(replies);

            return RepliesResponseDto.builder()
                    .repliesId(updatedComment.getId())
                    .content(updatedComment.getContent())
                    .modifiedDate(updatedComment.getModifiedDate())
                    .build();
    }

    @Transactional
    public void realDeleteReplies(Long repliesId, String email) {
        RepliesEntity replies = checkRepliesOwnerShip(repliesId, email);
            repliesRepository.delete(replies);
    }
}