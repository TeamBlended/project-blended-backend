package com.gdsc.blended.jwt.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gdsc.blended.common.message.ApiResponse;
import com.gdsc.blended.common.message.DefaultMessage;
import com.gdsc.blended.common.message.ResponseMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException{
        // 401에러
        ResponseMessage errorMessage = DefaultMessage.UNAUTHORIZED;
        ApiResponse apiResponse = ApiResponse.message(errorMessage, null);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(apiResponse);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(errorMessage.getStatusCode().value());
        response.getWriter().write(json);
    }
}
