package com.gdsc.blended.common.exception;

import com.gdsc.blended.common.apiResponse.ApiResponse;
import com.gdsc.blended.common.apiResponse.DefaultMessage;
import com.gdsc.blended.common.apiResponse.ResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerAdviceExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<?> handleApiException(ApiException e) {
        return makeErrorResponse(e.getResponseMessage());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleArgumentTypeMismatch(final MethodArgumentTypeMismatchException e) {
        final ResponseMessage errorMessage = DefaultMessage.BAD_REQUEST;
        return makeErrorResponse(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleIllegalArgument(final IllegalArgumentException e) {
        final ResponseMessage errorMessage = DefaultMessage.INTERNAL_SERVER_ERROR;
        return makeErrorResponse(errorMessage);
    }

    // Request Body와 형식이 맞지 않거나, JSON 형태를 지키지 않았을 경우 발생한다.
    @Override
    public ResponseEntity<Object> handleHttpMessageNotReadable(
            HttpMessageNotReadableException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {
        final ResponseMessage errorMessage = DefaultMessage.INVALID_JSON;
        return makeErrorResponse(errorMessage);
    }

    @Override
    public ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException e,
            final HttpHeaders headers,
            final HttpStatusCode status,
            final WebRequest request
    ) {
        final ResponseMessage errorMessage = DefaultMessage.INVALID_PARAMETER;
        return makeErrorResponse(errorMessage);
    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<Object> handleAllException(final Exception ex) {
        System.out.println(ex.getMessage());
        final ResponseMessage errorMessage = DefaultMessage.INTERNAL_SERVER_ERROR;
        return makeErrorResponse(errorMessage);
    }

    private ResponseEntity<Object> makeErrorResponse(ResponseMessage errorMessage) {
        ApiResponse<?> errorResponse = ApiResponse.message(errorMessage, null);
        return ResponseEntity.status(errorMessage.getStatusCode()).body(errorResponse);
    }
}
