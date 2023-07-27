package com.gdsc.blended.common.exception;


import com.gdsc.blended.common.apiResponse.ResponseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final ResponseMessage responseMessage;
}