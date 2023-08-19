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
import org.springframework.stereotype.Service;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    private final TokenProvider tokenProvider;
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse googleLogin(String idToken){
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new ApiException(AuthMessage.LOGIN_BAD_REQUEST);
            }
            else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                if(!userRepository.existsByEmail(userInfo.getEmail())){
                    throw new ApiException(UserResponseMessage.USER_NOT_FOUND);
                }else{
                    return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());
                }
            }
        } catch (Exception e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    @Transactional
    public TokenResponse reissue(String email, String name, String refreshToken) throws Exception {
        validateRefreshToken(refreshToken);

        Optional<UserEntity> userEntityOptional = userRepository.findByEmail(email);

        if (userEntityOptional.isPresent()) {
            UserEntity userEntity = userEntityOptional.get();

            if (!userEntity.getName().equals(name)) {
                userEntity.setName(name);
                userRepository.save(userEntity);
            }
        } else {
            throw new ApiException(UserResponseMessage.USER_NOT_FOUND);
        }

        TokenResponse tokenResponse = createToken(email, name);
        return tokenResponse;
    }

    private TokenResponse sendGenerateJwtToken(String email, String name) {
        TokenResponse tokenResponse = createToken(email, name);
        return tokenResponse;
    }

    private void validateRefreshToken(String refreshToken){
        if(!tokenProvider.validateToken(refreshToken))
            throw new ApiException(UserResponseMessage.REFRESH_TOKEN_INVALID);
    }

    private TokenResponse createToken(String email, String name) {
        return tokenProvider.generateJwtToken(email, name, RoleType.MEMBER);
    }

}
