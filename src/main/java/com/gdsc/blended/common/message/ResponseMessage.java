package com.gdsc.blended.common.message;

import org.springframework.http.HttpStatus;

public interface ResponseMessage {
    String getMessage();
    HttpStatus getStatusCode();
}
