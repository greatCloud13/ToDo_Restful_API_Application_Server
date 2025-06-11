package com.example.jwt.dto;

/*
*   JWT 토큰 응답 DTO
*   로그인 성공 시 client에게 반환되는 토큰 정보
 */

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtResponse {

    /*
    *   Access Token
    *   API 요청 시 인증에 사용되는 단기 토큰
     */
    private String accessToken;

    /*
    *   Refresh Token
    *   Access Token 갱신에 사용되는 장기 토큰
     */
    private String refreshToken;

    /*
    * 토큰 타입 (보통 "Bearer")
     */
    @Builder.Default
    private String tokenType = "Bearer";

    /*
    * Access Token  만료 시간 (초)
     */

    private Long expiresIn ;

    /*
    *   토큰 발급 시간
     */
    @Builder.Default
    private LocalDateTime issuedAt = LocalDateTime.now();

    /*
    *   사용자 정보
     */
    private String username;

    /*
    *   사용자 권한 목록
     */
    private List<String> authorities;

    /*
    * Access Token만으로 간단한 응답 생성
     */
    public static JwtResponse of(String accessToken, String username, Long expiresIn){
        return JwtResponse.builder()
                .accessToken(accessToken)
                .username(username)
                .expiresIn(expiresIn)
                .build();
    }

    /*
    *   전체 토큰 정보로 응답 생성
     */
    public static JwtResponse of(String accessToken, String refreshToken, String username, Long expiresIn,
                                 List<String> authorities){
        return JwtResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .username(username)
                .expiresIn(expiresIn)
                .authorities(authorities)
                .build();
    }
}
