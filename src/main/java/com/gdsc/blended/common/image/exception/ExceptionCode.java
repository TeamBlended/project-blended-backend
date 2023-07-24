package com.gdsc.blended.common.image.exception;

import lombok.Getter;

public enum ExceptionCode {
    IMAGE_NOT_FOUND(404, "Image not found"), IMAGE_UPLOAD_FAILED(500, "Image upload failed"), IMAGE_DELETE_FAILED(500, "Image delete failed");


    @Getter
    private final int status;

    @Getter
    private final String message;

    ExceptionCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
