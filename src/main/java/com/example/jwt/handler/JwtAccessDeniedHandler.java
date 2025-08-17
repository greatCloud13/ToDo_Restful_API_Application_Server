package com.example.jwt.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.print.attribute.HashPrintJobAttributeSet;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * JWT 접근 거부 핸들러
 * 인증은 성공했지만 권한이 부족한 사용자의 접근을 처리
 * 403 Forbidden 응답을 JSON 형태로 반환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    /**
     * 접근 거부시 호출되는 메소드
     *
     * @param request 요청
     * @param response 응답
     * @param accessDeniedException 접근 거부 예외
     * @throws IOException 응답 작성 실패
     */
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException{

        String requestURI = request.getRequestURI();
        String username = getCurrentUsername();

        log.warn("접근 권한 부족: 사용자: {}, URI: {}, Error: {}", username, requestURI, accessDeniedException.getMessage());

        // 응답 설정
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");

        // 에러 응답 데이터 생성
        Map<String, Object> errorResponse = createErrorResponse(request, accessDeniedException);

        // JSON 응답 작성
        String jsonResponse = objectMapper.writeValueAsString(errorResponse);
        response.getWriter().write(jsonResponse);

        log.debug("접근 거부 응답 전송 완료 : 사용자={}, URI={},", username, requestURI);

    }

    private Map<String, Object> createErrorResponse(HttpServletRequest request,
                                                    AccessDeniedException accessDeniedException){
        Map<String, Object> errorResponse = new HashMap<>();

        //기본 에러 정보
        errorResponse.put("timestamp", LocalDateTime.now().toString());
        errorResponse.put("status", HttpServletResponse.SC_FORBIDDEN);
        errorResponse.put("error", "Forbidden");
        errorResponse.put("message", determineErrorMessage());
        errorResponse.put("path", request.getRequestURI());

        // 추가 정보
        errorResponse.put("method", request.getMethod());
        errorResponse.put("errorCode", "JWT_ACCESS_DENID");
        errorResponse.put("username", getCurrentUsername());

        // 사용자 권한 정보 (디버깅용)
        if(log.isDebugEnabled()){
            errorResponse.put("authorities", getCurrentUserAuthorities());
            errorResponse.put("userAgent", request.getHeader("User-Agent"));
            errorResponse.put("remoteAddr", getClientIpAddress(request));
        }

        return errorResponse;
    }


    /**
     * 권한 부족 상황에 적합한 에러 메시지 결정
     *
     * @return 사용자 친화적 에러 메시지
     */
    private String determineErrorMessage(){
        String username = getCurrentUsername();

        if(username != null && !username.equals("anonymousUser")){
            return "해당 리소스에 접근할 권한이 없습니다";
        } else {
            return "인증이 필요한 서비스입니다. 로그인 후 이용해주세요.";
        }
    }


    /**
     * 현재 인증된 사용자명 추출
     *
     * @return 사용자명 (인증되지 않은 경우 null)
     */
    private String getCurrentUsername(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.isAuthenticated()
        && !authentication.getName().equals("anonymousUser")){
            return authentication.getName();
        }

        return null;
    }

    /**
     * 현재 사용자의 권한 목록 추출 (디버깅용)
     *
     * @return 권한 목록 문자열
     */
    private String getCurrentUserAuthorities(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null && authentication.getAuthorities() != null){
            return authentication.getAuthorities().stream()
                    .map(authority -> authority.getAuthority())
                    .collect(Collectors.joining(","));
        }

        return "NONE";
    }

    /**
     * 클라이언트 IP 주소 호출
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
        if(xRealIp != null && !xRealIp.isEmpty()&&!"unkown".equalsIgnoreCase(xRealIp)){
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}
