package com.example.jwt.provider;

import com.example.jwt.config.JwtProperties;
import com.example.jwt.dto.JwtResponse;
import com.example.jwt.exception.ExpiredJwtTokenException;
import com.example.jwt.exception.InvalidJwtTokenException;
import com.example.jwt.exception.UnsupportedJwtTokenException;
import com.example.jwt.util.JwtConstants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

/*  JWT 토큰 생성/검증/파싱 **핵심 클래스**
*   JJWT 라이브러리 사용
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenProvider {
    private final JwtProperties jwtProperties;
    private SecretKey secretKey;

    /**
    *   시크릿 키 초기화
    *   Base64로 인코딩된 키를 SecretKey 객체로 변환
     */
    @PostConstruct
    protected void init(){
        this.secretKey = Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes());
        log.info("JWT SecretKey 초기화 완료");
    }

    /**
    *   Access Token 생성
    *   @param authentication 인증정보
    *   @return JWT Access Token
     */
    public String generateAccessToken(Authentication authentication){
        return generateToken(authentication, jwtProperties.getAccessTokenExpiration(), JwtConstants.ACCESS_TOKEN_TYPE);
    }

    /**
    *   Refresh Token 생성
    *
    * @param authentication 인증 정보
    * @return JWT Refresh Token
     */
    public String generateRefreshToken(Authentication authentication){
        return generateToken(authentication, jwtProperties.getRefreshTokenExpiration(), JwtConstants.REFRESH_TOKEN_TYPE);
    }

    /**
    *   Username 으로 Access Token 생성 (간편 메소드)
    *
    * @param username 사용자명
    * @param authorities 권한 목록
    * @return JWT Access Token
     */
    public String generateAccessToken(String username, Collection<? extends GrantedAuthority> authorities){
        return generateToken(username, authorities, jwtProperties.getAccessTokenExpiration(), JwtConstants.ACCESS_TOKEN_TYPE);
    }

    /**
     *  Username 으로 Refresh Token 생성 (간편 메소드)
     *
     * @param username 사용자명
     * @param authorities 권한 목록
     * @return JWT Refresh Token
     */
    public String generateRefreshToken(String username, Collection<? extends GrantedAuthority> authorities){
        return generateToken(username, authorities, jwtProperties.getRefreshTokenExpiration(), JwtConstants.REFRESH_TOKEN_TYPE);
    }

    /**
    *   JWT 토큰 응답 객체 생성
    *
    * @param username 사용자명
    * @param authorities 권한 목록
    * @return JwtResponse 객체
     */
    public JwtResponse generateTokenResponse(String username, Collection<? extends GrantedAuthority> authorities){
        String accessToken = generateAccessToken(username, authorities);
        String refreshToken = generateRefreshToken(username, authorities);

        return JwtResponse.of(
                accessToken,
                refreshToken,
                username,
         jwtProperties.getAccessTokenExpiration() / 1000, // 초 단위로 변환
                authorities.stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    /**
     * 토큰에서 Authentication 객체 생성
     *
     * @param token JWT 토큰
     * @return Authentication 객체
     */
    public Authentication getAuthentication(String token){
        Claims claims = parseClaims(token);

        if(claims.get(JwtConstants.CLAIM_AUTHORITIES) == null){
            throw new InvalidJwtTokenException("권한 정보가 없는 토큰입니다");
        }

        //권한 정보 추출
        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(JwtConstants.CLAIM_AUTHORITIES).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        //UserDetails 객체 생성
        UserDetails principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
    }

    /**
     * 토큰에서 사용자명 추출
     *
     * @param token JWT 토큰
     * @return 사용자명
     */
    public String getUsername(String token){
        return parseClaims(token).getSubject();
    }

    /**
     * 토큰 유효성 검증
     *
     * @param token JWT 토큰
     * @return 유효 여부
     */
    public boolean validateToken(String token){
        try{
            parseClaims(token);
            return true;
        }catch (ExpiredJwtException e){
            log.warn("만료된 JWT 토큰: {}", e.getMessage());
            throw new ExpiredJwtTokenException("토큰이 만료되었습니다", e);
        }catch(UnsupportedJwtException e){
            log.warn("지원하지 않는 JWT 토큰: {}", e.getMessage());
            throw new UnsupportedJwtTokenException("지원하지 않는 토큰입니다", e);
        }catch(MalformedJwtException e){
            log.warn("잘못된 JWT 토큰: {}", e.getMessage());
            throw new InvalidJwtTokenException("잘못된 형식의 토큰입니다", e);
        }catch(SecurityException e){
            log.warn("JWT 서명이 유효하지 않습니다: {}", e.getMessage());
            throw new InvalidJwtTokenException("토큰 서명이 유효하지 않습니다", e);
        }catch(IllegalArgumentException e){
            log.warn("JWT 토큰이 비어있습니다: {}", e.getMessage());
            throw new InvalidJwtTokenException("토큰이 비어있습니다", e);
        }
    }

    /**
     * 토큰 타입 검증 (Access/Refresh 구분)
     *
     * @param token JWT 토큰
     * @param expectedType 예상 토큰 타입
     * @return 검증 결과
     */
    public boolean validateTokenType(String token, String expectedType){
        Claims claims = parseClaims(token);
        String tokenType =claims.get(JwtConstants.CLAIM_TOKEN_TYPE, String.class);
        return expectedType.equals(tokenType);
    }

    /**
     * 토큰 만료 시간 추출
     *
     * @param token JWT 토큰
     * @return 만료 시간 (밀리초)
     */
    public Long getExpirationTime(String token){
        Claims claims = parseClaims(token);
        return claims.getExpiration().getTime();
    }

    /**
     * 실제 토큰 생성 로직
     *
     * @param authentication 인증 정보
     * @param expiration 만료 시간
     * @param tokenType 토큰 타입
     * @return JWT 토큰
     */
    private String generateToken(Authentication authentication, long expiration, String tokenType){
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return generateToken(authentication.getName(), authorities, expiration, tokenType);
    }

    /**
     *  사용자명과 권한으로 토큰 생성
     *
     * @param username 사용자명
     * @param authorities 권한 목록
     * @param expiration 만료 시간
     * @param tokenType 토큰 타입
     * @return JWT 토큰
     */
    private String generateToken(String username, Collection<? extends GrantedAuthority> authorities, long expiration, String tokenType){
        String authoritiesStr = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return generateToken(username, authoritiesStr, expiration, tokenType);
    }

    /**
     * 실제 토큰 생성 메서드
     *
     * @param username 사용자명
     * @param authorities 권한 무자열
     * @param expiration 만료시간
     * @param tokenType 토큰 타입
     * @return JWT 토큰
     */
    private String generateToken(String username, String authorities, Long expiration, String tokenType){
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(username)
                .claim(JwtConstants.CLAIM_AUTHORITIES, authorities)
                .claim(JwtConstants.CLAIM_TOKEN_TYPE, tokenType)
                .setIssuer(jwtProperties.getIssuer())
                .setAudience(jwtProperties.getAudience())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }


    /**
     * 토큰 파싱 및 Claim 추출
     *
     * @param token JWT 토큰
     * @return Claims 객체
     */
    private Claims parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }



}
