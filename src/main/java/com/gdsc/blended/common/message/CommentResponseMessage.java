package com.gdsc.blended.common.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum CommentResponseMessage implements ResponseMessage{
    COMMENT_CREATED("댓글 작성 성공", HttpStatus.OK),
    COMMENT_UPDATE("댓글 수정 성공", HttpStatus.OK),
    COMMENT_DELETE("댓글 삭제 성공", HttpStatus.OK),
    COMMENT_FIND_ONE("댓글 조회 성공", HttpStatus.OK),
    COMMENT_FIND_ALL("댓글 전체 조회 성공", HttpStatus.OK),
    COMMENT_NOT_FOUND("댓글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus statusCode;

    CommentResponseMessage(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }
}
