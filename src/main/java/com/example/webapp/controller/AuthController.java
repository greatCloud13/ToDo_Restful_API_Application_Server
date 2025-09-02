package com.example.webapp.controller;

import com.example.jwt.Service.AuthService;
import com.example.jwt.dto.JwtResponse;
import com.example.jwt.dto.LoginRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 인증 관련 API 컨트롤러
 * 로그인, 회원가입, 토큰 갱신, 로그아웃 등의 endpoint 제공
 */
@Slf4j
@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * 사용자 로그인
     *
     * @param loginRequest 로그인 요청 정보
     * @return JWT 토큰 응답
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        log.info("로그인 요청: {}", loginRequest.getUsername());

        JwtResponse jwtResponse = authService.login(loginRequest);

        return ResponseEntity.ok(jwtResponse);
    }

}
