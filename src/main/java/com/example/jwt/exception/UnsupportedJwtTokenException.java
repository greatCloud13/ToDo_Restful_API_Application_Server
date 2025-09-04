package com.example.jwt.exception;

import com.example.jwt.util.JwtConstants;

/*
*   지원하지 않는 JWT 토큰 예외
*   토큰의 알고리즘이나 형식이 지원되지 않는 경우 발생
 */
public class UnsupportedJwtTokenException extends JwtException{

    /*
    *   기본 메시지로 예외 생성
     */
    public UnsupportedJwtTokenException(){
        super(JwtConstants.UNSUPPORTED_TOKEN_MESSAGE, 401, "UNSUPPORTED_JWT_TOKEN");
    }

    /*
    *   커스텀 메시지로 예외 생성
     */
    public UnsupportedJwtTokenException(String message){
        super(message, 401, "UNSUPPORTED_JWT_TOKEN");
    }

    /*
    *   원인 예외와 함께 생성
     */
    public UnsupportedJwtTokenException(String message, Throwable cause){
        super(message, cause, 401, "UNSUPPORTED_JWT_TOKEN");
    }

    /*
    *   지원하지 않는 알고리즘 정보와 함께 메시지 생성
     */
    public static UnsupportedJwtTokenException withAlgorithm(String algorithm){
       return new UnsupportedJwtTokenException(
        String.format("지원하지 않는 JWT 알고리즘 입니다: %s", algorithm)
       );
    }
}
