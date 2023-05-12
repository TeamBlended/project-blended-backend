package com.gdsc.blended.comment.dto;

import com.gdsc.blended.comment.entity.CommentEntity;
import com.gdsc.blended.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentRequestDto {
    private Long postId;
    private String content;


}
