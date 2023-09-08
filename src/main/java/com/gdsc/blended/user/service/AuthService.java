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
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

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
                    UserEntity userEntity = new UserEntity(userInfo, randomNickname());
                    userRepository.save(userEntity);
                } else {
                    UserEntity user = userRepository.findByEmail(email).orElseThrow(() ->
                            new ApiException(UserResponseMessage.USER_NOT_FOUND));
                    if (user.getNickname() == null) {
                        UserEntity userEntity = new UserEntity(userInfo, randomNickname());
                        userRepository.save(userEntity);
                    }
                }
                return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());
            }
        }catch (ApiException e) {
            throw e;
        } catch (Exception e) {
            throw new ApiException(AuthMessage.INVALID_TOKEN);
        }
    }

    private String randomNickname() {
        final String[] adjectives = {
                "친절한", "잘생긴", "똑똑한", "용감한", "우아한", "행복한"
        };

        final String[] nouns = {
                "고양이", "강아지", "호랑이", "사자", "펭귄", "악어"
        };

        Random random = new Random();
        String adjective = adjectives[random.nextInt(adjectives.length)];
        String noun = nouns[random.nextInt(nouns.length)];
        int number;

        // 중복되지 않는 닉네임을 생성하기 위해 중복 체크
        String nickname;
        do {
            number = random.nextInt(1000);
            nickname = adjective + noun + "#" + number;
        } while (userRepository.existsByNickname(nickname));

        return nickname;
    }

    @Transactional
    public TokenResponse reissue(String email, String name, String refreshToken) {
        validateRefreshToken(refreshToken);
        message = UserResponseMessage.REISSUE_SUCCESS.getMessage();
        return createToken(email, name );
    }

    private TokenResponse sendGenerateJwtToken(String email, String name ) {
        return createToken(email, name );
    }

    private void validateRefreshToken(String refreshToken){
        if(!tokenProvider.validateToken(refreshToken))
            throw new ApiException(UserResponseMessage.REFRESH_TOKEN_INVALID);
    }

    private TokenResponse createToken(String email, String name) {
        return tokenProvider.generateJwtToken(email, name, RoleType.MEMBER);
    }


    @Transactional
    public void withdrawUser(String email){
        UserEntity userEntity = userRepository.findByEmail(email).orElseThrow(() ->
                new ApiException(UserResponseMessage.USER_NOT_FOUND));

        userEntity.setWithdrawalDate(LocalDateTime.now());
        userRepository.save(userEntity);
    }

}
