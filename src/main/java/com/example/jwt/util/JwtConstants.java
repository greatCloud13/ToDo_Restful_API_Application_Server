package com.example.jwt.util;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * JWT 관련 상수를 정의하는 클래스
 * 토큰 헤더, 클레임, 타입 등의 상수값들을 중앙 집중 관리
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstants {
//    === HTTP 헤더 관련 ===
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer" ;

//    === JWT 클레임 키 ===
    public static final String CLAIM_USER_ID = "userId";
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_AUTHORITIES = "authorities";
    public static final String CLAIM_ISSUED_AT = "iat";
    public static final String CLAIM_EXPIRES_AT = "exp";

//    === 토큰 타입 ===
    public static final String ACCESS_TOKEN_TYPE = "ACCESS";
    public static final String REFRESH_TOKEN_TYPE = "REFRESH";

//    === 에러 메시지 ===
    public static final String INVALID_TOKEN_MESSAGE = "유효하지 않은 토큰입니다";
    public static final String EXPIRED_TOKEN_MESSAGE = "만료된 토큰 입니다";
    public static final String UNSUPPORTED_TOKEN_MESSAGE = "지원하지 않는 토큰입니다";
    public static final String MALFORMED_TOKEN_MESSAGE = "잘못된 형식의 토큰입니다";
    public static final String EMPTY_TOKEN_MESSAGE = "토큰이 비어있습니다";
    public static final String SIGNATURE_INVALID_MESSAGE = "토큰 서명이 유효하지 않습니다";

//    === 응답 메시지 ===
    public static final String LOGIN_SUCCESS_MESSAGE = "로그인에 성공하였습니다";
    public static final String LOGOUT_SUCCESS_MESSAGE = "로그아웃에 성공하였습니다";
    public static final String DEFAULT_SECRET_KEY = "defaultSecretKeyForJwtTokenGenerationAndValidation"; //ToDo 실무에서는 경해야함

//    === 정규식 패턴 ===
    public static final String BEARER_TOKEN_PATTERN = "^Bearer\\s+(.+)$";


//    === 토큰이 필요없는 URL 패턴 ===
    public static final String[] PUBLIC_URLS = {
        "/api/auth/login",
        "/api/auth/signup",
        "/api/auth/refresh",
        "/h2-console/**",
        "/swagger-ui/**",
        "/v3/api-docs/**"
    };
}
