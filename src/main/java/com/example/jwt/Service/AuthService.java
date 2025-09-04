package com.example.jwt.Service;

import com.example.jwt.Repository.UserRepository;
import com.example.jwt.config.JwtProperties;
import com.example.jwt.dto.JwtResponse;
import com.example.jwt.dto.LoginRequest;
import com.example.jwt.dto.SignupRequest;
import com.example.jwt.exception.InvalidJwtTokenException;
import com.example.jwt.provider.JwtTokenProvider;
import com.example.webapp.entity.RefreshToken;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

/**
 * 인증 관련 비즈니스 로직을 처리하는 Service
 * 로그인, 회원가입, 토큰 갱신, 로그아웃 기능 포함
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenService refreshTokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProperties jwtProperties;

    /**
     * 사용자 회원가입
     *
     * @param request 회원가입 요청정보
     * @return 생성된 사용자 정보
     */
    @Transactional
    public User signup(SignupRequest request){
        log.debug("회원가입 시도:{}({})", request.getUsername(), request.getEmail());

//        1. 비밀번호 일치 확인
        if(!request.isPasswordMatching()){
            throw new InvalidJwtTokenException("비밀번호와 비밀번호 확인이 일치하지 않습니다");
        }

//        2. 사용자명 중복 확인
        if(userRepository.existsByUsername(request.getUsername())){
            throw new InvalidJwtTokenException("이미 사용중인 사용자명입니다: "+request.getUsername());
        }
//        3. 이메일 중복 확인
        if(userRepository.existsByEmail(request.getEmail())){
            throw new InvalidJwtTokenException("이미 사용중인 이메일입니다: "+request.getEmail());
        }
//        4. 새 사용자 생성
        User newUser = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword() ))
                .role(Role.USER) //기본값 일반 사용자
                .enabled(true)
                .build();

        User savedUser = userRepository.save(newUser);

        log.info("회원가입 완료: {} ({})", savedUser.getUsername(), savedUser.getEmail());
        return savedUser;
    }


    /**
     * 사용자 로그인
     *
     * @param request 로그인 요청 정보
     * @return JWT 토큰 응답
     */
    @Transactional
    public JwtResponse login(LoginRequest request){
        log.debug("로그인 시도: {}", request.getUsername());

        try{
//            1. 사용자 인증
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

//            2. JWT 토큰 생성
            JwtResponse jwtResponse = jwtTokenProvider.generateTokenResponse(
                    authentication.getName(),
                    authentication.getAuthorities()
            );

//            3. Refresh Token을 DB에 저장
            User user = userRepository.findByUsername(request.getUsername())
                    .orElseThrow(()-> new InvalidJwtTokenException("사용자를 찾을 수 없습니다."));

            refreshTokenService.createRefreshToken(user.getId());

            log.info("로그인 성공: {}", request.getUsername());
            return jwtResponse;
        } catch (AuthenticationException e){
            log.warn("로그인 실패: {}-{}", request.getUsername(), e.getMessage());
            throw new BadCredentialsException("사용자명 또는 비밀번호가 올바르지 않습니다.", e);
        }
    }


    /**
     * Refresh Token으로 새로운 Access Token 발급
     * @param refreshTokenStr Refresh Token 문자열
     * @return 새로운 JWT 토큰 응답
     */
    @Transactional
    public JwtResponse refreshToken(String refreshTokenStr){
        log.debug("토큰 갱신 요청");

        try{
            // 1. refresh Token 검증 및 새 토큰 생성
            RefreshToken newRefreshToken = refreshTokenService.createRefreshToken((refreshTokenStr));

            // 2. 사용자 정보 추출
            User user = newRefreshToken.getUser();

            // 3. 새 JWT 토큰 응답 생성 (Access Token + 새 Refresh Token)
            String newAccessToken = jwtTokenProvider.generateAccessToken(
                    user.getUsername(),
                    Collections.singletonList(
                            new SimpleGrantedAuthority(
                                    user.getRole().getAuthority()
                            )
                    )
            );

            // 4. JwtResponse 생성
            JwtResponse jwtResponse = JwtResponse.builder()
                    .accessToken(newAccessToken)
                    .refreshToken(newRefreshToken.getToken())
                    .username(user.getUsername())
                    .expiresIn(jwtProperties.getAccessTokenExpiration() / 1000)
                    .authorities(Collections.singletonList(user.getRole().getAuthority()))
                    .message("토큰이 성공적으로 생성되었습니다")
                    .build();

            log.info("토큰 갱신 완료: {}", user.getUsername());
            return jwtResponse;
        } catch (Exception e){
            log.warn("토큰 갱신 실패: {}", e.getMessage());
            throw new InvalidJwtTokenException("토큰 갱신에 실패했습니다: "+e.getMessage(), e);
        }
    }


    /**
     * 사용자 로그아웃
     * 해당 사용자의 모든 Refresh Token을 삭제
     *
     * @param username 로그아웃 사용자명
     */
    @Transactional
    public void logout(String username){
        log.debug("로그아웃 요청, 사용자: {}", username);

        try {
            //해당 사용자의 모든 Refresh Token 삭제
            refreshTokenService.deleteByUsername(username);

            log.info("로그가웃 완료: {}", username);
        } catch (Exception e){
            log.warn("로그아웃 처리 중 오류 발생: {} - {}", username, e.getMessage());
            // 클라이언트는 성공으로 응답됨
        }
    }

    /**
     * 사용자명 중복 확인
     *
     * @param username 확인할 사용자명
     * @return 사용 가능 여부 (true: 사용 가능, false: 이미 사용 중)
     */
    @Transactional(readOnly = true)
    public boolean isUsernameAvailable(String username) {
        return !userRepository.existsByUsername(username);
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 확인할 이메일
     * @return 사용 가능 여부 (true: 사용 가능, false: 이미 사용 중)
     */
    @Transactional(readOnly = true)
    public boolean isEmailAvailable(String email) {
        return !userRepository.existsByEmail(email);
    }



































}
