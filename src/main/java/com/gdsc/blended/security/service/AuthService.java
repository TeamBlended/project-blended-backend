package com.gdsc.blended.security.service;

import com.gdsc.blended.security.dto.TokenDto;
import com.gdsc.blended.security.dto.TokenRequestDto;
import com.gdsc.blended.security.dto.UserRequestDto;
import com.gdsc.blended.security.provider.JwtTokenizer;
import com.gdsc.blended.security.refreshToken.RefreshToken;
import com.gdsc.blended.security.refreshToken.RefreshTokenRepository;
import com.gdsc.blended.user.mapper.UserMapper;
import com.gdsc.blended.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final UserRepository userRepository;
    private final JwtTokenizer jwtTokenizer;
    private final RefreshTokenRepository refreshTokenRepository;

    //oauth 회원가입


    //로그인 시 토큰 발급
    @Transactional
    public TokenDto login(UserRequestDto userRequestDto) {
        // 1.Login ID/PW 를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = userRequestDto.toAuthentication();

        // 2.실제로 검증 (사용자 비밀번호 체크) 이 이루어지는 부분
        // authenticate 메서드가 실행이 될 때 CustomUserDetailsService에서 만들었던 loadUserByUsername 메서드가 실행됨
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 3.인증 정보를 기반으로 JWT 토큰 생성
        TokenDto tokenDto = createToken(authentication);

        // 4.RefreshToken 저장
        RefreshToken createdRefreshToken = RefreshToken.builder()
                .key(authentication.getName())
                .value(tokenDto.getRefreshToken())
                .build();
        refreshTokenRepository.save(createdRefreshToken);

        // 5.토큰 발급
        return tokenDto;
    }


    //토큰 재발급
    @Transactional
    public TokenDto reissue(TokenRequestDto tokenRequestDto) {
        // 1.Refresh Token 검증
        if (!jwtTokenizer.validateToken(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("Refresh Token 이 유효하지 않습니다.");
        }

        // 2.Access Token 에서 인증정보 가져오기
        Authentication authentication = jwtTokenizer.getAuthentication(tokenRequestDto.getAccessToken());

        // 3.저장소에서 User ID(email) 를 기반으로 Refresh Token 값 가져옴
        RefreshToken refreshToken =
                refreshTokenRepository.findByKey(authentication.getName())
                        .orElseThrow(() -> new RuntimeException("로그아웃 된 사용자입니다."));

        // 4.유저의 Refresh Token 과 저장된 Refresh Token 이 일치하는지 검사
        if (!refreshToken.getValue().equals(tokenRequestDto.getRefreshToken())) {
            throw new RuntimeException("토큰의 유저 정보가 일치하지 않습니다.");
        }

        // 5.새로운 토큰 생성
        TokenDto tokenDto = createToken(authentication);

        // 6.저장소 정보 업데이트
        RefreshToken newRefreshToken = refreshToken.updateValue(tokenDto.getRefreshToken());
        refreshTokenRepository.save(newRefreshToken);

        //토큰 발급
        return tokenDto;
    }

    // 클래스 내부에서만 사용 가능한 토큰 생성하는 로직
    private TokenDto createToken(Authentication authentication) {
        String accessToken = jwtTokenizer.generateAccessToken(authentication);
        String refreshToken = jwtTokenizer.generateRefreshToken(authentication);
        TokenDto tokenDto = new TokenDto(accessToken, refreshToken);
        return tokenDto;
    }

}
