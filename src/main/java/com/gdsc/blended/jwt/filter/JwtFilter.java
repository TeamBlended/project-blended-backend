package com.gdsc.blended.jwt.filter;

import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.common.message.AuthMessage;
import com.gdsc.blended.jwt.token.TokenProvider;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;


import java.io.IOException;

import static com.gdsc.blended.common.message.AuthMessage.EXPIRED_TOKEN;

@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        try {
            String jwt = resolveToken(request);

            if (StringUtils.hasText(jwt)) {
                if (tokenProvider.validateToken(jwt)) {
                    Authentication authentication = tokenProvider.getAuthentication(jwt);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                } else {
                    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                    throw new ApiException(AuthMessage.EXPIRED_TOKEN);
                }
            }
            filterChain.doFilter(request, response);
        } catch (ApiException e) {
            // ApiException이 발생한 경우에 대한 예외처리
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(e.getMessage());
        } catch (Exception e) {
            // 그 외의 예외에 대한 처리
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Internal Server Error");
            e.printStackTrace();
        }
    }

    /*private void sendUnauthorizedResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.getWriter().write(message);
    }

    private void sendInternalServerErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        response.getWriter().write(message);
    }*/

    /*@Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String jwt = resolveToken(request);

        if (StringUtils.hasText(jwt)) {
            if (tokenProvider.validateToken(jwt)) {
                Authentication authentication = tokenProvider.getAuthentication(jwt);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new ApiException(AuthMessage.EXPIRED_TOKEN);
            }
        }
        filterChain.doFilter(request, response);
    }*/

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
