package com.gdsc.blended.common.apiResponse;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AlcoholResponseMessage implements ResponseMessage{
    ALCOHOL_IMAGE_UPLOAD_SUCCESS("주류 이미지 업로드 성공", HttpStatus.OK),
    ALCOHOL_IMAGE_UPLOAD_FAIL("주류 이미지 업로드 실패", HttpStatus.INTERNAL_SERVER_ERROR),
    ALCOHOL_NOT_FOUND("존재하지 않는 주류 정보 입니다.", HttpStatus.NOT_FOUND);
    //IMAGE_NOT_FOUND("이미지가 존재하지 않습니다.", HttpStatus.NOT_FOUND);

    private final String message;
    private final HttpStatus statusCode;
}
