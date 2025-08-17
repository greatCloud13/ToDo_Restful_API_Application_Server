package com.example.jwt.config;

import com.example.jwt.filter.JwtAuthenticationFilter;
import com.example.jwt.handler.JwtAccessDeniedHandler;
import com.example.jwt.handler.JwtAuthenticationEntryPoint;
import com.example.jwt.provider.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * JWT 관련 bean 설정 클래스
 * JWT 인증에 필요한 모든 컴포넌트들을 Spring 빈으로 등록
 */
@Configuration
@RequiredArgsConstructor
public class JwtConfiguration {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 비밀번호 암호와 인코더 빈 등록
     *  BCrypt 알고리즘 사용 (강력한 해시 알고리즘)
     *
     * @return BCryptPasswordEncoder 인스턴스
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * JWT 인증 필터 빈 등록
     * 모든 HTTP 요청에서 JWT 토큰을 검증하는 필터
     *
     * @return JwtAuthenticationFilter 인스턴스
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    /**
     * JWT 인증 진입점 빈 등록
     * 인증되지 않은 사용자가 보호된 리소스에 접근할 때 처리
     *
     * @param objectMapper JSON 변환용 ObjectMapper
     * @return JwtAuthenticationEntryPoint 인스턴스
     */
    @Bean
    public JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint(ObjectMapper objectMapper){
        return new JwtAuthenticationEntryPoint(objectMapper);
    }

    /**
     * JWT 접근 거부 핸들러 빈 등록
     * 인증은 성공했지만 권한이 부족한 경우 처리
     *
     * @param objectMapper JSON 변환용 ObjectMapper
     * @return JwtAccessDeniedHandler 인스턴스
     */
    @Bean
    public JwtAccessDeniedHandler jwtAccessDeniedHandler(ObjectMapper objectMapper){
        return new JwtAccessDeniedHandler(objectMapper);
    }

    /**
     * ObjectMapper 빈 등록
     * JSON 직렬화/역직렬화에 사용
     *
     * @return ObjectMapper 인스턴스
     */
    @Bean
    public ObjectMapper objectMapper(){
        return new ObjectMapper();
    }
}
