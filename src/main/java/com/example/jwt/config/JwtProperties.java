package com.example.jwt.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * JWT 관련 설정값을 설정값에서 바딘딩 하는 클래스
 * @ConfigurationProperties를 사용하여 Typesafe하게 설정 관리
 * ToDo Properties 파일로 분리 필요
 */

@Data
@Component
@ConfigurationProperties(prefix = "app.jwt")
public class JwtProperties {

    /*
    *   JWT 서명에 사용할 비밀 키
    *   환경변수나 외부 설정으로 관리하는 것을 권장함
    */
    private String secret = "jwtSecretKey123456789";

    /*
    *   Access Token 만료 시간 (단위: ms)
    *   기본값: 15분 (900,000ms)
     */
    private long accessTokenExpiration = 15 * 60 * 1000L;

    /*
     *  Refresh Token 만료 시간 (단위: ms)
     * 기본값: 7일 ( 604,800,)
     */
    private long refreshTokenExpiration = 7 * 24 * 60 * 1000L;

    /*
    *   Authorization 헤더 토큰 접두사
    *   기본값: "Bearer"
     */
    private String tokenPrefix = "Bearer";

    /*
    * Authorization 헤더명
    *   기본값 "Authorization"0
    */
    private String headerName = "Authorization";

    /*
    *   JWT 발급자 정보
    *   기본값: "webapp-api
     */
    private String issuer = "webapp-api";

    /*
    *   JWT 대상 정보
    *   기본값: "webapp-users
     */
    private String audience = "webapp-users";

}
