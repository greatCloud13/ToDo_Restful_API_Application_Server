package com.example.webapp.common.exception;

import com.example.jwt.exception.JwtException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 전역 예외 처리 핸들러
 * 애플리케이션에서 발생하는 모든 예외를 중앙에서 처리
 */
@Slf4j
//@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * JWT 관련 예외 처리
     *
     * @param ex JWT 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(JwtException.class)
    public ResponseEntity<Map<String, Object>> handleJwtException(
            JwtException ex, HttpServletRequest request) {

        log.warn("JWT 예외 발생: {} - URI: {}", ex.getMessage(), request.getRequestURI());

        Map<String, Object> errorResponse = createErrorResponse(
                ex.getStatusCode(),
                ex.getErrorCode(),
                ex.getMessage(),
                request.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatusCode()).body(errorResponse);
    }

    /**
     * 인증 실패 예외 처리 (잘못된 자격 증명)
     *
     * @param ex 인증 실패 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Map<String, Object>> handleBadCredentialsException(
            BadCredentialsException ex, HttpServletRequest request) {

        log.warn("인증 실패: {} - URI: {}", ex.getMessage(), request.getRequestURI());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "AUTHENTICATION_FAILED",
                "사용자명 또는 비밀번호가 올바르지 않습니다.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 사용자를 찾을 수 없는 예외 처리
     *
     * @param ex 사용자 없음 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleUsernameNotFoundException(
            UsernameNotFoundException ex, HttpServletRequest request) {

        log.warn("사용자 조회 실패: {} - URI: {}", ex.getMessage(), request.getRequestURI());

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.UNAUTHORIZED.value(),
                "USER_NOT_FOUND",
                "존재하지 않는 사용자입니다.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    /**
     * 유효성 검증 실패 예외 처리 (@Valid 검증 실패)
     *
     * @param ex 유효성 검증 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(
            MethodArgumentNotValidException ex, HttpServletRequest request) {

        log.warn("유효성 검증 실패 - URI: {}", request.getRequestURI());

        Map<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            fieldErrors.put(fieldName, errorMessage);
        });

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                "VALIDATION_FAILED",
                "입력값 검증에 실패했습니다.",
                request.getRequestURI()
        );

        errorResponse.put("fieldErrors", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    /**
     * 일반적인 Runtime 예외 처리
     *
     * @param ex Runtime 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(
            RuntimeException ex, HttpServletRequest request) {

        log.error("예상치 못한 런타임 예외 발생: {} - URI: {}", ex.getMessage(), request.getRequestURI(), ex);

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "INTERNAL_SERVER_ERROR",
                "서버 내부 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 최상위 예외 처리 (모든 예외의 fallback)
     *
     * @param ex 예외
     * @param request HTTP 요청
     * @return 에러 응답
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(
            Exception ex, HttpServletRequest request) {

        log.error("예상치 못한 예외 발생: {} - URI: {}", ex.getMessage(), request.getRequestURI(), ex);

        Map<String, Object> errorResponse = createErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "UNEXPECTED_ERROR",
                "예상치 못한 오류가 발생했습니다.",
                request.getRequestURI()
        );

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    /**
     * 공통 에러 응답 생성
     *
     * @param status HTTP 상태 코드
     * @param errorCode 에러 코드
     * @param message 에러 메시지
     * @param path 요청 경로
     * @return 에러 응답 Map
     */
    private Map<String, Object> createErrorResponse(int status, String errorCode, String message, String path) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", status);
        errorResponse.put("error", getErrorName(status));
        errorResponse.put("errorCode", errorCode);
        errorResponse.put("message", message);
        errorResponse.put("path", path);

        return errorResponse;
    }

    /**
     * HTTP 상태 코드에 따른 에러명 반환
     *
     * @param status HTTP 상태 코드
     * @return 에러명
     */
    private String getErrorName(int status) {
        return switch (status) {
            case 400 -> "Bad Request";
            case 401 -> "Unauthorized";
            case 403 -> "Forbidden";
            case 404 -> "Not Found";
            case 500 -> "Internal Server Error";
            default -> "Error";
        };
    }
}