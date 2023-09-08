package com.gdsc.blended.common.message;

import com.gdsc.blended.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private HttpStatus statusCode;
    private String message;


    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(data, HttpStatus.OK, "SUCCESS");
    }

    public static <T> ApiResponse<T> error(HttpStatus code, String message) {
        return new ApiResponse<>(null, code, message);
    }
    public static <T> ApiResponse<T> error(ResponseMessage responseMessage){
        return new ApiResponse<>(null, responseMessage.getStatusCode(), responseMessage.getMessage());
    }

    public static <T> ApiResponse<T> message(ResponseMessage responseMessage, T data){
        return new ApiResponse<>(data, responseMessage.getStatusCode(), responseMessage.getMessage());
    }

}
