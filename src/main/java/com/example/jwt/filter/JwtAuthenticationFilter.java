package com.example.jwt.filter;

import com.example.jwt.provider.JwtTokenProvider;
import com.example.jwt.util.JwtConstants;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;

/**
 * JWT 인증 필터
 * 모든 HTTP 요청을 가로채서 JWT 토큰을 검증하고 SecurityContext에 인증 정보 설정
 * OncePerRequestFilter를 상속하여 요청당 한 번만 실행되도록 보장
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 실제 필터링 로직 수행
     *
     * @param request HTTP 요청
     * @param response HTTP 응답
     * @param filterChain 필터 체인
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        try{
            // 1. 요청에서 JWT 토큰 추출
            String token = resolveToken(request);

            // 2. 토큰이 존재하면 유효성 검증
            if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){

                // 3.Access Token 인지 확인 (Refresh Token 제외)
                if (jwtTokenProvider.validateTokenType(token, JwtConstants.ACCESS_TOKEN_TYPE)){

                    // 4. 토큰으로부터 Authentication 객체 생성
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);

                    // 5. SecurityContext 에 인증정보 설정
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    log.debug("Security Context 에 '{}' 인증 정보를 저장했습니다. URI: {}",
                            authentication.getName(), request.getRequestURI());
                } else {
                    log.warn("Access Token 이 아닌 토큰으로 AIP 접근 시도: {}", request.getRequestURI());
                }
            }else {
                log.warn("유효한 JWT 토큰이 없습니다 URI: {}", request.getRequestURI());
            }
        }catch(Exception e){
            log.error("인증 정보를 설정할 수 없습니다: {}", e.getMessage());
            // 예외 발생 시 SecurityContext 초기화
            SecurityContextHolder.clearContext();
        }

        // 6. 다음 필터로 진행
        filterChain.doFilter(request, response);
    }

    /**
     * HTTP 요청에서 JWT 토큰 추출
     * Authorization 헤더에서 Bearer 토큰 형식으로 추출
     *
     * @param request HTTP 요청
     * @return JWT 토큰 (Bearer 접두사 제거)
     */
    private String resolveToken(HttpServletRequest request){
        String bearerToken = request.getHeader(JwtConstants.AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(JwtConstants.BEARER_PREFIX)){
            return bearerToken.substring(JwtConstants.BEARER_PREFIX.length());
        }
        return null;
    }

    /**
     * 특정 요청에 대해 필터를 건너뛸 지 결정
     * 공개 URL이나 정적 리소스는 JWT 검증을 생략
     *
     * @param request HTTP 요청
     * @return 필터 건너뛸 여부
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){
        String path = request.getRequestURI();

        // 공개 url 체크
        return Arrays.stream(JwtConstants.PUBLIC_URLS)
                .anyMatch(path::startsWith)||
                //정적 리소스 체크
                path.startsWith("/css/")||
                path.startsWith("/js/")||
                path.startsWith("/favicon.ico");
    }
}
