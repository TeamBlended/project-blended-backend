package com.gdsc.blended.common.apiResponse;

import com.gdsc.blended.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse<T> {
    private T data;
    private HttpStatus statusCode;
    private String message;

    public ApiResponse(UserEntity updatedUser, UserResponseMessage nicknameUpdateSuccess) {
        //이런식으로 가면 나중에 관리하기 힘들어요 그냥 Message 만들어놨으니까 그거 최대한 활용하는 방식으로 하는게 제일 편하지 않을까..?합니당당
    }

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

/**
 * @Getter
 * @AllArgsConstructor
 * public class JMTApiResponse<T> {
 *     T data;
 *     String message;
 *     String code;
 *
 *     public static <G> JMTApiResponse<G> createResponseWithMessage(G data, ResponseMessage responseMessage) {
 *         return new JMTApiResponse<>(data, responseMessage.getMessage(), responseMessage.toString());
 *     }
 * }
 *
 *
 * @Getter
 * @RequiredArgsConstructor
 * public class ApiException extends RuntimeException{
 *     private final ResponseMessage responseMessage;
 * }
 * */