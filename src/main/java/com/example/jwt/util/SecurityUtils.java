package com.example.jwt.util;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Spring Security 관련 유틸리티 클래스
 * 현재 인증된 사용자 정보를 쉽게 가져오는 메소드 제공
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityUtils {

    private final UserRepository userRepository;

    /**
     * 현재 인증된 사용자의 Authentication 객체 반환
     * @return Authentication 객체(인증되지 않은 경우 null)
     */
    public static Authentication getCurrentAuthentication(){
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 현재 인증된 사용자명 반환
     * @return 사용자명 (인증되지 않은 경우 null)
     */
    public static String getCurrentUsername() {
        Authentication authentication = getCurrentAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return null;
        }

        String username = authentication.getName();

        return "anonymousUser".equals(username) ? null : username;
    }

    /**
     * 현재 사용자의 인증 여부
     * @return 인증 여부
     */
    public static boolean isAuthenticated(){
        Authentication authentication = getCurrentAuthentication();

        return authentication != null
                && authentication.isAuthenticated()
                && !authentication.getName().equals("anonymousUser");
    }

    /**
     * 현재 사용자가 특정 권한을 가지고 있는지 확인
     * @param authority 확인할 권한 (예: "ROLE_ADMIN")
     * @return 권한 보유 여부
     */
    public static boolean hasAuthority(String authority){
        Authentication authentication = getCurrentAuthentication();

        if(authentication == null || !authentication.isAuthenticated()){
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));

    }

    /**
     * 현재 사용자가 관리자인지 확인
     * @return 관리자 여부
     */
    public static boolean isAdmin(){
        return hasAuthority("ROLE_ADMIN");
    }

    /**
     * 현재 사용자가 일반 사용자 이상의 권한을 가지고 있는지 확인
     *
     * @return 사용자 이상 권한 여부
     */
    public static boolean isUserOrAbove() {
        return hasAuthority("ROLE_USER") || hasAuthority("ROLE_ADMIN");
    }

    /**
     * 현재 인증된 사용자의 User 엔티티 반환
     *
     * @return User 엔티티 (Optional)
     */
    public Optional<User> getCurrentUser() {
        String username = getCurrentUsername();

        if (username == null) {
            log.debug("인증된 사용자가 없습니다.");
            return Optional.empty();
        }

        return userRepository.findByUsername(username);
    }

    /**
     * 현재 인증된 사용자의 User 엔티티 반환 (예외 처리)
     *
     * @return User 엔티티
     * @throws IllegalStateException 인증된 사용자가 없는 경우
     */
    public User getCurrentUserOrThrow() {
        return getCurrentUser()
                .orElseThrow(() -> new IllegalStateException("인증된 사용자를 찾을 수 없습니다."));
    }

    /**
     * 현재 사용자의 ID 반환
     *
     * @return 사용자 ID (Optional)
     */
    public Optional<Long> getCurrentUserId() {
        return getCurrentUser().map(User::getId);
    }

    /**
     * 현재 사용자의 ID 반환 (예외 처리)
     *
     * @return 사용자 ID
     * @throws IllegalStateException 인증된 사용자가 없는 경우
     */
    public Long getCurrentUserIdOrThrow() {
        return getCurrentUserId()
                .orElseThrow(() -> new IllegalStateException("인증된 사용자 ID를 찾을 수 없습니다."));
    }

    /**
     * 특정 사용자가 현재 로그인한 사용자와 같은지 확인
     *
     * @param userId 확인할 사용자 ID
     * @return 동일 사용자 여부
     */
    public boolean isCurrentUser(Long userId) {
        return getCurrentUserId()
                .map(currentUserId -> currentUserId.equals(userId))
                .orElse(false);
    }

    /**
     * 현재 사용자가 특정 리소스에 접근할 수 있는지 확인
     * (본인 리소스이거나 관리자인 경우)
     *
     * @param resourceOwnerId 리소스 소유자 ID
     * @return 접근 가능 여부
     */
    public boolean canAccessResource(Long resourceOwnerId) {
        return isAdmin() || isCurrentUser(resourceOwnerId);
    }
}
