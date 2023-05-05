package com.gdsc.blended.config;

import com.gdsc.blended.security.filter.JwtAuthenticationFilter;
import com.gdsc.blended.security.provider.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@RequiredArgsConstructor
public class JwtSecurityConfig extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {
    private final JwtTokenizer jwtTokenizer;

    //JwtTokenizer 를 주입받아 JwtFilter를 통해 Security 로직에 필터를 등록
    //커스텀한 jwtAuthenticationFilter를 Security Filter 앞에 추가
    @Override
    public void configure(HttpSecurity http) {
        JwtAuthenticationFilter jwtFilter = new JwtAuthenticationFilter(jwtTokenizer);
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
    }
}
