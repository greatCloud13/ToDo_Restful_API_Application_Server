package com.example.webapp.config;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * 개발 환경용 초기 데이터 로더
 * 애플리케이션 시작 시 테스트용 사용자 데이터를 생성
 */
@Slf4j
@Component
@Profile({"dev","prod","docker"})
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        loadInitialData();
    }

    /**
     * 초기 데이터 생성
     */
    private void loadInitialData() {
        log.info("개발환경 초기 데이터 생성 시작...");

        // 기존 데이터 확인
        if (userRepository.count() > 0) {
            log.info("이미 사용자 데이터가 존재합니다. 초기 데이터 생성을 건너뜁니다.");
            return;
        }

        // 관리자 계정 생성
        createAdminUser();

        // 일반 사용자 계정 생성
        createTestUsers();

        log.info("개발환경 초기 데이터 생성 완료!");
    }

    /**
     * 관리자 계정 생성
     */
    private void createAdminUser() {
        User admin = User.builder()
                .username("admin")
                .email("admin@example.com")
                .password(passwordEncoder.encode("Qlalfqjsgh123!"))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);
        log.info("관리자 계정 생성 완료");
    }

    /**
     * 테스트용 일반 사용자 계정들 생성
     */
    private void createTestUsers() {
        // 일반 사용자 1
        User user1 = User.builder()
                .username("user1")
                .email("user1@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.USER)
                .enabled(true)
                .build();

        // 일반 사용자 2
        User user2 = User.builder()
                .username("user2")
                .email("user2@example.com")
                .password(passwordEncoder.encode("user123"))
                .role(Role.USER)
                .enabled(true)
                .build();

        // 비활성화된 사용자 (테스트용)
        User disabledUser = User.builder()
                .username("disabled")
                .email("disabled@example.com")
                .password(passwordEncoder.encode("disabled123"))
                .role(Role.USER)
                .enabled(false)
                .build();

        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(disabledUser);

        log.info("테스트 사용자 계정 생성 완료:");
        log.info("- user1 (활성): username=user1, password=user123");
        log.info("- user2 (활성): username=user2, password=user123");
        log.info("- disabled (비활성): username=disabled, password=disabled123");
    }
}