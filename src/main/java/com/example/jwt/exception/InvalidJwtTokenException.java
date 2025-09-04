package com.example.jwt.exception;

import com.example.jwt.util.JwtConstants;

/*
*   유효하지 않은 JWT 토큰 예외
*   토큰 변조, 올바르지 않은 형식일 때 발생
 */
public class InvalidJwtTokenException extends JwtException {
    /*
    *기본 메시지로 예외 생성
     */
    public InvalidJwtTokenException(){
        super(JwtConstants.INVALID_TOKEN_MESSAGE, 401, "INVALID_JWT_TOKEN");
    }

    /*
    *   커스텀 메시지로 예외 생성
     */
    public InvalidJwtTokenException(String message){
        super(message, 401, "INVALID_JWT_TOKEN");
    }

    /*
    *   원인 예외와 함께 생성
     */
    public InvalidJwtTokenException(String message, Throwable cause){
        super(message, cause, 401, "INVALID_JWT_TOKEN");
    }

    /*
    * 토큰 값과 함께 상세한 메시지 생성
     */
    public static InvalidJwtTokenException withToken(String token){
        return new InvalidJwtTokenException(
                String.format("유효하지 않은 JWT 토큰 입니다: %s",
                        token != null && token.length() > 10 ?
                                token.substring(0,10)+ "...": token)
                        );
    }
}
