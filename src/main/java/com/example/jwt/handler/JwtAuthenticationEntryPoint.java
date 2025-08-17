package com.example.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * JWT 인증 진입점
 * 인증되지 않은 사용자가 보호된 리소스에 접근할 떄 호출되는 핸들러
 * 401 Unauthorized 응답을 JSON 형태로 반환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 호출되는 메서드
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param authException 인증 예외
     * @throws IOException 응답 작성 실패
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {

        String requestURI = request.getRequestURI();
        log.warn("인증되지 않은 접근 시도: {} - {}", requestURI, authException.getMessage());

        //응답 설정
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        //에러 응답 데이터 생성
        Map<String, Object> errorResponse = createErrorResponse(request, authException);

        // JSON 응답 작성
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);

        log.debug("인증 실패 응답 전송 완료: {}", requestURI);
    }

    /**
     * 에러 응답 데이터 생성
     *
     * @param request HTTP 요청
     * @param authException 인증 예외
     * @return 에러 응답 Map
     */
    private Map<String, Object> createErrorResponse(HttpServletRequest request,
                                                    AuthenticationException authException){
        Map<String, Object> errorResponse = new HashMap<>();

        //기본 에러 정보
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpServletResponse.SC_UNAUTHORIZED);
        errorResponse.put("error", "Unauthorized");
        errorResponse.put("message", determineErrorMessage(authException));
        errorResponse.put("path", request.getRequestURI());

        //추가 정보
        errorResponse.put("method", request.getMethod());
        errorResponse.put("errorCode", "JWT_AUTHENTICATION_FAILED");

        //요청 헤더 정보(디버깅용)
        if (log.isDebugEnabled()) {
            errorResponse.put("userAgent", request.getHeader("User-Agent"));
            errorResponse.put("remoteAddress", getClientIpAddress(request));
        }

        return errorResponse;
    }

    /**
     * 예외 타입에 따른 적절한 에러 메시지 결정
     *
     * @param authException 인증 예회
     * @return 사용자 친화적 에러 메시지
     */
    private String determineErrorMessage(AuthenticationException authException){
        String message = authException.getMessage();

        //JWT 관련 예외 메시지 처리
        if(message != null){
            if(message.contains("expired")){
                return "토큰이 만료되었습니다. 다시 로그인 해주세요.";
            }else if(message.contains("invalid")||message.contains("malformed")){
                return "유효하지 않은 토큰입니다.";
            } else if (message.contains("signature")) {
                return "토큰 서명이 유효하지 않습니다";
            } else if(message.contains("unsupported")){
                return "지원하지 않는 토큰 형식입니다";
            }
        }


        // 기본 메시지
        return "인증에 필요한 서비스 입니다. 로그인 후 이용해주세요.";
    }

    /**
     * client Ip 주소 추출
     * 프록시 환경을 고려한 실제 IP 주소 추출
     *
     * @param request HTTP 요청
     * @return 클라이언트 IP 주소
     */
    private String getClientIpAddress(HttpServletRequest request){
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if(xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)){
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if(xRealIp != null && !xRealIp.isEmpty() && !"unkown".equalsIgnoreCase(xRealIp)){
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
