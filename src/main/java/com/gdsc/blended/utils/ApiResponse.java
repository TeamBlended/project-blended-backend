package com.gdsc.blended.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private int code;
    private String message;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, 200, "SUCCESS");
    }

    public static <T> ApiResponse<T> error(int code, String message) {
        return new ApiResponse<>(null, code, message);
    }

}
