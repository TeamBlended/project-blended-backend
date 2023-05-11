package com.gdsc.blended.exception;

import lombok.Getter;

public enum ExceptionCode {
    IMAGE_NOT_FOUND(404, "Image not found");

    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
