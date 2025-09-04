package com.example.jwt.exception;

import lombok.Getter;

/*
*   JWT 관련 최상위 예외 클래스
*   모든 JWT 예외의 부모 클래스로 공통 속성과 메서드 제공
 */
@Getter
public class JwtException extends RuntimeException{

    /*
    *   HTTP 상태 코드
     */
    private final int statusCode;

    /*
    * 에러코드 (내부적으로 사용)
     */
    private final String errorCode;

    /*
    *   기본 생성자
     */
    public JwtException(String message){
        super(message);
        this.statusCode = 401; //Unauthorized
        this.errorCode = "JWT_ERROR";
    }

    /*
    * 상태 코드와 함께 생성
     */
    public JwtException(String message, int statusCode){
        super(message);
        this.statusCode = statusCode;
        this.errorCode = "JWT_ERROR";
    }

    /*
    *   완전한 정보와 함께 생성
     */
    public JwtException(String message, int statusCode, String errorCode){
        super(message);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }

    /*
     *   원인 예외와 함께 생성
     */
    public JwtException(String message, Throwable cause){
        super(message, cause);
        this.statusCode = 401;
        this.errorCode = "JWT_ERROR";
    }

    /*
    *   모든 정보와 함께 생성
     */
    public JwtException(String message, Throwable cause, int statusCode, String errorCode){
        super(message, cause);
        this.statusCode = statusCode;
        this.errorCode = errorCode;
    }



}
