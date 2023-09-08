package com.gdsc.blended.user.service;

import com.gdsc.blended.common.message.AuthMessage;
import com.gdsc.blended.common.message.UserResponseMessage;
import com.gdsc.blended.common.exception.ApiException;
import com.gdsc.blended.jwt.dto.TokenResponse;
import com.gdsc.blended.jwt.oauth.GoogleOAuth2UserInfo;
import com.gdsc.blended.jwt.token.TokenProvider;
import com.gdsc.blended.user.entity.RoleType;
import com.gdsc.blended.user.entity.UserEntity;
import com.gdsc.blended.user.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;
    private String message = null;
    @Transactional
    public TokenResponse googleLogin(String idToken){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.LOGIN_BAD_REQUEST);
            } else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());
                String email = userInfo.getEmail();

                if (!userRepository.existsByEmail(userInfo.getEmail())) {
                    UserEntity userEntity = new UserEntity(userInfo);
                    userRepository.save(userEntity);
                    message = UserResponseMessage.SIGNIN_SUCCESS.getMessage();
                } else {
                    UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                            new ApiException(UserResponseMessage.USER_NOT_FOUND));
                    if (user.getNickname() == null) {
                        message = UserResponseMessage.NICKNAME_NOT_PROVIDED.getMessage();
                    }
                }
                return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName(), message);
            }
        }catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    @Transactional
    public TokenResponse reissue(String email, String name, String refreshToken) {
        validateRefreshToken(refreshToken);
        message = UserResponseMessage.REISSUE_SUCCESS.getMessage();
        return createToken(email, name, message);
    }

    private TokenResponse sendGenerateJwtToken(String email, String name, String message) {
        return createToken(email, name, message);
    }

    private void validateRefreshToken(String refreshToken){
        if(!tokenProvider.validateToken(refreshToken))
            throw new ApiException(UserResponseMessage.REFRESH_TOKEN_INVALID);
    }

    private TokenResponse createToken(String email, String name, String message ) {
        return tokenProvider.generateJwtToken(email, name, RoleType.MEMBER, message);
    }


    @Transactional
    public void withdrawUser(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(UserResponseMessage.USER_NOT_FOUND));

        userEntity.setWithdrawalDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

}
