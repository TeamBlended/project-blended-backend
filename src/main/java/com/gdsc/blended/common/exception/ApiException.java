package com.gdsc.blended.common.exception;


import com.gdsc.blended.common.message.AuthMessage;
import com.gdsc.blended.common.message.ResponseMessage;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
//@RequiredArgsConstructor
public class ApiException extends RuntimeException{
    private final ResponseMessage responseMessage;


    public ApiException(ResponseMessage responseMessage) {
        super(responseMessage.getMessage());
        this.responseMessage = responseMessage;
    }

    /*public int getHttpStatus() {
        return responseMessage.getStatusCode().value();
    }*/
}