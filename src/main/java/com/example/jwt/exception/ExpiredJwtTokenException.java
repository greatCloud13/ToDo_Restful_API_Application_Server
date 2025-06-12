package com.example.jwt.exception;

import com.example.jwt.util.JwtConstants;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/*
*   만료된 JWT 토큰 예외
*   토큰의 만료 시간이 지났을 때 발생
 */
public class ExpiredJwtTokenException extends JwtException{

    /*
    *   기본 메시지로 예외 생성
     */
    public ExpiredJwtTokenException(){
        super(JwtConstants.EXPIRED_TOKEN_MESSAGE, 401, "EXPIRED_JWT_TOKEN");
    }

    /*
    *   커스텀 메시지로 예외 새성
     */
    public ExpiredJwtTokenException(String message){
        super(message, 401, "EXPIRED_JWT_TOKEN");
    }

    /*
    *   원인 예외와 함께 생성
     */
    public ExpiredJwtTokenException(String message, Throwable cause){
        super(message, cause, 401, "EXPIRED_JWT_TOKEN");
    }

    /*
    *   만료 시간과 함께 상세한 메시지 생성
     */
    public static ExpiredJwtTokenException withExpiredTime(LocalDateTime expiredAt){
        String formattedTime = expiredAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return new ExpiredJwtTokenException(
                String.format("JWT 토큰이 만료되었습니다. 만료 시간: %s", formattedTime)
        );
    }

}
