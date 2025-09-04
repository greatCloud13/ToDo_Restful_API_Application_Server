package com.example.jwt.provider;

import com.example.jwt.dto.JwtResponse;
import com.example.jwt.exception.InvalidJwtTokenException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * JWT 인증을 위한 커스텀 AuthenticationProvider
 * Spring Security의 인증 과정에서 jwt 토큰 기반 인증을 처리
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;


    /**
     * 인증 처리 메서드
     * 사용자명과 비밀번호를 검증하고 JWT 토큰 생성
     *
     * @param authentication 인증 요청 정보
     * @return 인증된 Authentication 객체
     * @throws AuthenticationException 인증 실패 시
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        log.debug("JWT 인증 시도: {}",username);

        try{
            //1. 사용자 정보 로드
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            //2. 비밀번호 검증
            if(!passwordEncoder.matches(password, userDetails.getPassword())){
                log.warn("비밀번호 불일치: {}", username);
                throw new InvalidJwtTokenException("비밀번호가 일치하지 않습니다.");
            }

            //3. 인증 성공 - Authentication 객체 생성
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,   //인증 후에는 비밀번호를 null로 설정
                            userDetails.getAuthorities()
                    );

            log.debug("JWT 인증 성공: {}", username);
            return authToken;
        }catch(Exception e){
            log.error("JWT 인증 실패: {} - {}", username, e.getMessage());
            throw new InvalidJwtTokenException("인증에 실패했습니다: "+e.getMessage(), e);
        }
    }


    /**
     * 이 Provider가 처리할 수 있는 Authentication 타입인지 확인
     * @param authentication Authentication 클래스 타입
     * @return 처리 가능 여부
     */
    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 사용자명과 비밀번호로 JWT 토큰 생성 (편의 메소드)
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @return JWT 토큰 응답
     * @throws AuthenticationException 인증 실패 시
     */
    public JwtResponse authenticationAndGenerateToken(String username, String password) throws AuthenticationException{
        //인증 객체 생성
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        //인증 수행
        Authentication authentication = authenticate(authRequest);

        //JWT 토큰 생성 및 반환
        return jwtTokenProvider.generateTokenResponse(
                authentication.getName(),
                authRequest.getAuthorities()
        );
    }

    /**
     * 사용자명과 비밀번호로 JWT 토큰 생성 (편의 메소드)
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @return JWT 토큰 응답
     * @throws AuthenticationException 인증 실패 시
     */
    public JwtResponse authenticateAndGenerateToken(String username, String password) throws AuthenticationException{

        //인증 객체 생성
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(username, password);

        //인증 수행
        Authentication authentication = authenticate(authRequest);

        //JWT 토큰 생성 및 반환
        return jwtTokenProvider.generateTokenResponse(
                authentication.getName(),
                authentication.getAuthorities()
        );
    }

    /**
     * 인증된 사용자 정보로 새 토큰 생성
     *
     * @param authentication 인증된 Authentication 객체
     * @return JWT 토큰 응답
     */
    public JwtResponse generateTokenFromAuthentication(Authentication authentication){
        return jwtTokenProvider.generateTokenResponse(
                authentication.getName(),
                authentication.getAuthorities()
        );
    }

    /**
     * 사용자 존재 여부 확인 (사전 검증용)
     *
     * @param username 사용자명
     * @return 존재 여부
     */
    public boolean userExists(String username){
        try{
            userDetailsService.loadUserByUsername(username);
            return true;
        }catch (Exception e){
            log.debug("사용자를 찾을 수 없습니다: {}", username);
            return false;
        }
    }
}
