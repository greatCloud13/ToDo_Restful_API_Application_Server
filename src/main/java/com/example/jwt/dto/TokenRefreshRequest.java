package com.example.jwt.dto;

/*
*   토큰 갱신 요청 DTO
*   Refresh Token을 사용하여 새로운 Access Token을 요청할 때 사용
 */

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.antlr.v4.runtime.Token;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenRefreshRequest {

    /*
    *   Refresh Token
    *   유효한 리프레시 토큰이여야 한다
     */
    @NotBlank(message = "Refresh Token is required")
    private String refreshToken;

    /*
    *   선택적으로 포함할 수 있는 추가 정보
    *   ex: 디바이스 정보, 앱 버전
     */
    private String deviceInfo;

    /*
    *   클라이언트 식별자 (선택 사항)
     */
    private String clientId;

    /*
    *   refreshToken 만 이용하는 생성자 메서드
     */
    public static TokenRefreshRequest of(String refreshToken){
        return TokenRefreshRequest.builder()
                .refreshToken(refreshToken)
                .build();
    }

    /*
    *   refresh, devideInfo를 포함하는 생성자 메서드
     */
    public static TokenRefreshRequest of(String refreshToken, String deviceInfo){
        return TokenRefreshRequest.builder()
                .refreshToken(refreshToken)
                .deviceInfo(deviceInfo)
                .build();
    }
}
