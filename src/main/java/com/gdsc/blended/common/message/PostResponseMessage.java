package com.gdsc.blended.common.message;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum PostResponseMessage implements ResponseMessage {
    POST_CREATED("게시글 작성 성공", HttpStatus.OK),
    POST_UPDATE("게시글 수정 성공", HttpStatus.OK),
    POST_DELETE("게시글 삭제 성공", HttpStatus.OK),
    POST_FIND_ONE("게시글 조회 성공", HttpStatus.OK),
    POST_FIND_ALL("게시글 전체 조회 성공", HttpStatus.OK),
    POST_SUCCESS_HEART("게시글 좋아요 성공", HttpStatus.OK),
    POST_SUCCESS_GET_LIST("게시글 리스트 가져오기 성공", HttpStatus.OK),



    POST_NOT_FOUND("게시글을 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    POST_NOT_MATCH("게시글이 일치하지 않습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_IMAGE("이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NOT_FOUND_LATANDLONG("위도와 경도를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    IMAGE_NOT_FOUND("이미지를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    IMAGE_DELETE_FAILED("이미지 삭제 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_UPLOAD_FAILED("이미지 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    IMAGE_TOO_LARGE("이미지 용량이 너무 큽니다.", HttpStatus.BAD_REQUEST),
    ;

    private final String message;
    private final HttpStatus statusCode;

    PostResponseMessage(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }


}
