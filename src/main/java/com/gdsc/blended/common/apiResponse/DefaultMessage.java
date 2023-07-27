package com.gdsc.blended.common.apiResponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum DefaultMessage implements ResponseMessage{

    INTERNAL_SERVER_ERROR("알수없는 서버 에러입니다.", HttpStatus.INTERNAL_SERVER_ERROR),
    UNAUTHORIZED("인증이 필요합니다.", HttpStatus.UNAUTHORIZED),
    FORBIDDEN("권한이 필요합니다.", HttpStatus.FORBIDDEN),
    INVALID_JSON("JSON 형식이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_PARAMETER("매개변수가 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    BAD_REQUEST("잘못된 요청입니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus statusCode;

    DefaultMessage(String message, HttpStatus statusCode) {
        this.message = message;
        this.statusCode = statusCode;
    }

}
