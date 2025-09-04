package com.example.webapp.controller;

import com.example.jwt.Service.AuthService;
import com.example.jwt.dto.*;
import com.example.jwt.util.JwtConstants;
import com.example.webapp.entity.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 사용자 회원가입
     *
     * @param signupRequest 회원가입 요청 정보
     * @return 가입 결과 메시지
     */
    public ResponseEntity<MessageResponse> signup(@Valid @RequestBody SignupRequest signupRequest){
        log.info("회원가입 요청: {} ({})", signupRequest.getUsername(), signupRequest.getEmail());

        boolean emailAvailable = authService.isEmailAvailable(signupRequest.getEmail());
        boolean usernameAvailable = authService.isUsernameAvailable(signupRequest.getUsername());
        boolean isInvitedUser = signupRequest.getInviteCode().equals(JwtConstants.INVITE_CODE);

        if (!isInvitedUser) {
            MessageResponse response = MessageResponse.failure("초대코드가 일치하지 않습니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (!emailAvailable) {
            MessageResponse response = MessageResponse.failure("이미 사용 중인 이메일입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        if (usernameAvailable) {
            MessageResponse response = MessageResponse.failure("이미 사용 중인 사용자명입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        User newUser = authService.signup(signupRequest);

        MessageResponse response = MessageResponse.success(
                "회원가입이 완료되었습니다. 로그인해주세요.",
                newUser.getUsername()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * Access Token 갱신
     *
     * @param request 토큰 갱신 요청 (Refresh Token 포함)
     * @return 새로운 JWT 토큰 응답
     */
    public ResponseEntity<JwtResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request){
        log.info("토큰 갱신 요청.");

        JwtResponse jwtResponse = authService.refreshToken(request.getRefreshToken());

        return ResponseEntity.ok(jwtResponse);
    }

    /**
     * 사용자 로그아웃
     * 현재 인증된 사용자의 Refresh Token을 모두 삭제
     *
     * @return 로그아웃 결과 메시지
     */
    @PostMapping("/logout")
    public ResponseEntity<MessageResponse> logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            log.info("로그아웃 요청: {}", username);

            authService.logout(username);

            MessageResponse response = MessageResponse.success("로그아웃이 완료되었습니다.");
            return ResponseEntity.ok(response);
        }

        // 인증되지 않은 상태에서도 로그아웃 성공으로 응답 (보안상)
        MessageResponse response = MessageResponse.success("로그아웃이 완료되었습니다.");
        return ResponseEntity.ok(response);
    }

    /**
     * 사용자명 중복 확인
     *
     * @param username 확인할 사용자명
     * @return 사용 가능 여부
     */
    @GetMapping("/check-username")
    public ResponseEntity<MessageResponse> checkUsername(@RequestParam String username) {
        log.debug("사용자명 중복 확인: {}", username);

        boolean available = authService.isUsernameAvailable(username);

        if (available) {
            MessageResponse response = MessageResponse.success("사용 가능한 사용자명입니다.");
            return ResponseEntity.ok(response);
        } else {
            MessageResponse response = MessageResponse.failure("이미 사용 중인 사용자명입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    /**
     * 이메일 중복 확인
     *
     * @param email 확인할 이메일
     * @return 사용 가능 여부
     */
    @GetMapping("/check-email")
    public ResponseEntity<MessageResponse> checkEmail(@RequestParam String email) {
        log.debug("이메일 중복 확인: {}", email);

        boolean available = authService.isEmailAvailable(email);

        if (available) {
            MessageResponse response = MessageResponse.success("사용 가능한 이메일입니다.");
            return ResponseEntity.ok(response);
        } else {
            MessageResponse response = MessageResponse.failure("이미 사용 중인 이메일입니다.");
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }
    }

    /**
     * 현재 인증 상태 확인
     * JWT 토큰이 유효한지 확인하는 엔드포인트
     *
     * @return 인증 상태 정보
     */
    @GetMapping("/status")
    public ResponseEntity<MessageResponse> getAuthStatus() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser")) {

            MessageResponse response = MessageResponse.success(
                    "인증된 사용자입니다.",
                    authentication.getName()
            );
            return ResponseEntity.ok(response);
        }

        MessageResponse response = MessageResponse.failure("인증되지 않은 사용자입니다.");
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }
}
