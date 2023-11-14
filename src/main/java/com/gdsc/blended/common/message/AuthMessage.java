package com.gdsc.blended.common.message;


import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AuthMessage implements ResponseMessage {
    LOGIN_BAD_REQUEST("잘못된 접근입니다.", HttpStatus.BAD_REQUEST),
    INVALID_TOKEN("ID TOKEN이 유효하지 않습니다", HttpStatus.UNAUTHORIZED),
    REISSUE_SUCCESS("토큰 재발급에 성공하였습니다.", HttpStatus.CREATED),
    REFRESH_TOKEN_INVALID("RefreshToken이 유효하지 않습니다.", HttpStatus.BAD_REQUEST),
    INVALID_JWT("잘못된 JWT 서명입니다.", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN("만료된 JWT 토큰입니다.", HttpStatus.UNAUTHORIZED),
    UNSUPPORTED_JWT("지원하지 않는 JWT입니다.", HttpStatus.BAD_REQUEST),
    WRONG_JWT_TOKEN("JWT 토큰이 잘못되었습니다.", HttpStatus.BAD_REQUEST);


    private final String message;
    private final HttpStatus statusCode;

    @Override
    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
