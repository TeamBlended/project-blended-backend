package com.gdsc.blended.comment.service;

import com.gdsc.blended.comment.dto.CommentRequestDto;
import com.gdsc.blended.comment.dto.CommentResponseDto;
import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.comment.replies.entity.RepliesEntity;
import com.gdsc.blended.comment.replies.repository.RepliesRepository;
import com.gdsc.blended.comment.repository.CommentRepository;
import com.gdsc.blended.common.message.CommentResponseMessage;
import com.gdsc.blended.common.message.PostResponseMessage;
import com.gdsc.blended.common.message.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.post.entity.ExistenceStatus;
import com.gdsc.blended.post.entity.PostEntity;
import com.gdsc.blended.post.repository.PostRepository;
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
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final RepliesRepository repliesRepository;

    @Transactional
    public CommentResponseDto createComment(CommentRequestDto requestDto, Long postId, String email) {
        PostEntity post = findPostByPostId(postId);
        UserEntity user = findUserByEmail(email);

        CommentEntity comment = CommentEntity.builder()
                .content(requestDto.getContent())
                .post(post)
                .existenceStatus(ExistenceStatus.EXIST)
                .user(user)
                .build();
        CommentEntity savedComment = commentRepository.save(comment);

        // Comment 엔티티 저장 로직
        AuthorDto authorDto = AuthorDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();
        return CommentResponseDto.builder()
                .commentId(savedComment.getId())
                .content(savedComment.getContent())
                .user(authorDto)
                .modifiedDate(savedComment.getModifiedDate())
                .build();
    }

    @Transactional
    public CommentResponseDto getComments(Long commentId, String email) {
        CommentEntity comment = findCommentByCommentId(commentId);
        UserEntity user = findUserByEmail(email);

        AuthorDto authorDto = AuthorDto.builder()
                .nickname(user.getNickname())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return CommentResponseDto.builder()
                .commentId(comment.getId())
                .content(comment.getContent())
                .user(authorDto)
                .modifiedDate(comment.getModifiedDate())
                .build();
    }

    @Transactional
    public Page<CommentResponseDto> getCommentListByPost(Long postId, Pageable pageable) {
        List<CommentEntity> comments;
        try {
            comments = commentRepository.findByPostId(postId);
        } catch (Exception e) {
            throw new ApiException(PostResponseMessage.POST_NOT_FOUND);
        }

        List<CommentResponseDto> commentResponseDtos = new ArrayList<>();

        if (comments.isEmpty())
            throw new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND);

        for (CommentEntity comment : comments) {
            CommentResponseDto commentResponseDto;
            if (comment.getExistenceStatus() == ExistenceStatus.NON_EXIST) {
                continue;
            } else {
                commentResponseDto = CommentResponseDto.builder()
                        .commentId(comment.getId())
                        .content(comment.getContent())
                        .user(AuthorDto.builder()
                                .nickname(comment.getUser().getNickname())
                                .profileImageUrl(comment.getUser().getProfileImageUrl())
                                .build())
                        .modifiedDate(comment.getModifiedDate())
                        .build();
            }
            commentResponseDtos.add(commentResponseDto);
        }

        return new PageImpl<>(commentResponseDtos);
    }

    @Transactional
    public CommentResponseDto updateComment(CommentRequestDto requestDto, Long commentId, String email) {
        CommentEntity comment = checkCommentOwnerShip(commentId, email);
        UserEntity user = findUserByEmail(email);
        {
            comment.updateContent(requestDto.getContent());
            CommentEntity updatedComment = commentRepository.save(comment);

            AuthorDto authorDto = AuthorDto.builder()
                    .nickname(user.getNickname())
                    .profileImageUrl(user.getProfileImageUrl())
                    .build();
            return CommentResponseDto.builder()
                    .commentId(updatedComment.getId())
                    .content(updatedComment.getContent())
                    .user(authorDto)
                    .modifiedDate(updatedComment.getModifiedDate())
                    .build();
        }
    }

    @Transactional
    public CommentResponseDto deleteComment(Long commentId, String email) {
        CommentEntity comment = checkCommentOwnerShip(commentId, email);
        comment.deleteStatus(ExistenceStatus.NON_EXIST);

        RepliesEntity replies = (RepliesEntity) repliesRepository.findByCommentId(commentId);
        replies.deleteReplies(ExistenceStatus.NON_EXIST);
        RepliesEntity deletedReplies = repliesRepository.save(replies);

        CommentEntity updatedComment = commentRepository.save(comment);

        AuthorDto authorDto = AuthorDto.builder()
                .nickname(comment.getUser().getNickname())
                .profileImageUrl(comment.getUser().getProfileImageUrl())
                .build();

        return CommentResponseDto.builder()
                .commentId(updatedComment.getId())
                .content(updatedComment.getContent())
                .modifiedDate(updatedComment.getModifiedDate())
                .user(authorDto)
                .build();
    }

    @Transactional
    public void realDeleteComment(Long commentId, String email) {
        CommentEntity comment = checkCommentOwnerShip(commentId, email);
        commentRepository.delete(comment);
    }

    public UserEntity findUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(UserResponseMessage.USER_NOT_FOUND));
    }

    public PostEntity findPostByPostId(Long postId) {
        return postRepository.findById(postId).orElseThrow(() ->
                new ApiException(PostResponseMessage.POST_NOT_FOUND));
    }

    public CommentEntity findCommentByCommentId(Long commentId) {
        return commentRepository.findById(commentId).orElseThrow(() ->
                new ApiException(CommentResponseMessage.COMMENT_NOT_FOUND));
    }

    private CommentEntity checkCommentOwnerShip(Long commentId, String email) {
        CommentEntity commentEntity = findCommentByCommentId(commentId);
        UserEntity user = findUserByEmail(email);
        if (!commentEntity.getUser().equals(user)) {
            throw new ApiException(UserResponseMessage.USER_NOT_MATCH);
        }
        return commentEntity;
    }
}

