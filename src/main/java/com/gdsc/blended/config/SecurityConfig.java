package com.gdsc.blended.config;

import com.gdsc.blended.security.filter.JwtAccessDeniedHandler;
import com.gdsc.blended.security.filter.JwtAuthenticationEntryPoint;
import com.gdsc.blended.security.provider.JwtTokenizer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.filter.CorsFilter;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig {
    private final CorsFilter corsFilter;
    private final JwtTokenizer jwtTokenizer;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()

                //exception handling 할때 만든 클래스 추가
                .exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint)
                .accessDeniedHandler(jwtAccessDeniedHandler)

                //h2-console 설정 추가
                .and()
                .headers()
                .frameOptions()
                .sameOrigin()

                //security는 기본적으로 session 사용, 세션 사용 안하니까 세션 설정을 Statelsee로 설정
                .and()
                .addFilter(corsFilter)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and()
                .authorizeRequests()
                .antMatchers("/signup").permitAll()   //회원가입을 위한 API는 토큰 없이도 허용
                .antMatchers(HttpMethod.POST, "/login").permitAll()  //로그인을 위한 API는 토큰 없이도 허용
                .antMatchers(HttpMethod.GET, "/**").permitAll()  //GET 요청은 토큰 없이도 허용
                .antMatchers("/h2/**").permitAll()
                .anyRequest().authenticated()  //나머지 API는 모두 인증 필요

                //JwtFilter를 addFilterBefore로 등록했던 JwtSecurityConfig 클래스를 적용
                .and()
                .apply(new JwtSecurityConfig(jwtTokenizer));
        return http.build();
    }
}
