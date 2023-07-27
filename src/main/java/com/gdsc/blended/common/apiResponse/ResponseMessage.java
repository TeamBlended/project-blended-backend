package com.gdsc.blended.common.apiResponse;

import org.springframework.http.HttpStatus;

public interface ResponseMessage {
    String getMessage();
    HttpStatus getStatusCode();
}
