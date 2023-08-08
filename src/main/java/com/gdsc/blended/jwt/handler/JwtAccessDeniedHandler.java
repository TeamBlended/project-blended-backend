package com.gdsc.blended.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.blended.common.message.ApiResponse;
import com.gdsc.blended.common.message.DefaultMessage;
import com.gdsc.blended.common.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException{
        // 403에러
        ResponseMessage errorMessage = DefaultMessage.FORBIDDEN;
        ApiResponse apiResponse = ApiResponse.message(errorMessage, null);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatusCode().value());
        response.getWriter().write(json);
    }
}
