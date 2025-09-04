package com.example.jwt.Service;

import com.example.webapp.entity.User;
import com.example.jwt.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;

/**
 * Spring Security와 현재 도메인의 User 엔티티를 연결하는 서비스
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
    private final UserRepository userRepository;

    /**
     * 사용자명으로 사용자 정보를 로드
     *
     * @param username 사용자명(이메일 또는 사용자 ID)
     * @return UserDetail 구현 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        log.debug("사용자 인증 시도:{}", username);

        //1. 데이터베이스에서 사용자 조회
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->{
                    log.warn("사용자를 찾을 수 없습니다: {}", username);
                    return new UsernameNotFoundException("사용자를 찾을 수 없습니다: "+"username");
                });
        //2. 사용자 상태 확인
        if(!user.isEnabled()){
            log.warn("비활성화된 사용자 로그인 시도{}", username);
            throw new UsernameNotFoundException("비활성화된 사용자입니다:" + username);
        }

        log.debug("사용자 정보 로드 성공: {}, 권한 {}", username, user.getRole());

        return createUserDetail(user);
    }

    /**
     * 사용자 ID로 사용자 정보를 로드 (JWT에서 사용)
     *
     * @param userId 사용자 ID
     * @return UserDetails 구현 객체
     * @throws UsernameNotFoundException 사용자를 찾을 수 없는 경우
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException{
        log.debug("사용자 ID로 인증 시도: {}", userId);

        User user = userRepository.findById(userId)
                .orElseThrow(()->{
                    log.warn("사용자 아이디를 찾을 수 없습니다: {}", userId);
                    return new UsernameNotFoundException("사용자 ID를 찾을 수 없습니다: "+ userId);
                });

        if(!user.isEnabled()){
            log.warn("비활성화된 사용자 ID: {}", userId);
            throw new UsernameNotFoundException("비활성화된 사용자 입니다");
        }
        return createUserDetail(user);
    }

    public UserDetails loadUserByEmail(String email) throws UsernameNotFoundException{
        log.debug("이메일로 사용자 조회: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{
                    log.warn("이메일을 찾을 수 없습니다: {}", email);
                    return new UsernameNotFoundException("이메일을 찾을 수 없습니다: "+email);
                        });
        if (!user.isEnabled()){
            log.warn("비활성화된 사용자 이메일: {}", email);
            throw  new UsernameNotFoundException("비활성화된 이메일 입니다: " + email);
        }
        return createUserDetail(user);
    }

    /**
     * User 엔티티를 Spring Security UserDetails로 변환
     *
     * @param user 사용자 엔티티
     * @return UserDetails 구현 객체
     */
    private UserDetails createUserDetail(User user){
        // 권한 정보 변환
        List<GrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_"+user.getRole().name())
        );

//        Spring Security User 객체 생성 및 반환
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

    /**
     * 사용자 존재 여부 확인
     *
     * @param username 사용자명
     * @return 존재 여부
     */
    @Transactional(readOnly = true)
    public boolean existsByUsername(String username){
        return userRepository.existsByUsername(username);
    }

    /**
     * 이메일을 통해 사용자 존재여부 확인
     *
     * @param email 이메일
     * @return 존재 여부
     */
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
}

