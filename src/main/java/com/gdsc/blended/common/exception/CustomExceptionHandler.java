package com.gdsc.blended.common.exception;

import com.gdsc.blended.common.message.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<Object>> handleApiException(ApiException ex) {
        ApiResponse<Object> apiResponse = ApiResponse.error(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return new ResponseEntity<>(apiResponse, HttpStatus.UNAUTHORIZED);
    }
}