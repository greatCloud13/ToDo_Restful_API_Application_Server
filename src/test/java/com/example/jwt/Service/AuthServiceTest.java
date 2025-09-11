package com.example.jwt.Service;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Test
    void 간단한_테스트(){
        assertTrue(true);
        assertEquals(2, 1+1);
        System.out.println("테스트가 실행되었습니다.");
    }

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void 사용자명_중복확인_사용가능한_경우() {
        // Arrange (준비)
        System.out.println("=========사용자명 중복확인 중복X 테스트==============");
        String username = "testuser";
        when(userRepository.existsByUsername(username)).thenReturn(false);

        // Act (실행)
        boolean result = authService.isUsernameAvailable(username);

        // Assert (검증)
        assertTrue(result, "사용 가능한 사용자명은 true를 반환해야 합니다");
        verify(userRepository).existsByUsername(username);

        System.out.println("✅ 사용자명 중복확인 테스트 통과!");
        System.out.println("================================================");
    }

    @Test
    void 이미_존재하는_사용자명으로_중복확인시_false를_반환한다() {
        // Arrange (준비)
        System.out.println("=========사용자명 중복확인 중복O 테스트==============");
        String username = "existUser";
        when(userRepository.existsByUsername(username)).thenReturn(true);

        // Act (실행)
        boolean result = authService.isUsernameAvailable(username);

        // Assert (검증)
        assertFalse(result,"사용 불가능한 사용자명은 false를 반환해야 합니다.");
        verify(userRepository).existsByUsername(username);


        System.out.println("✅ 사용자명 중복확인 테스트 통과!");
        System.out.println("================================================");
    }

    @Test
    void 이메일_중복확인_사용가능한_경우_true를_반환한다() {
        // Arrange (준비)
        System.out.println("=========이메일 중복확인 사용가능한 경우 true를 반환==============");
        String userEmail="testUser@test.com";
        when(userRepository.existsByEmail(userEmail)).thenReturn(false);

        // Act (실행)
        boolean result = authService.isEmailAvailable(userEmail);

        // Assert (검증)
        assertTrue(result, "사용 가능한 이메일은 true를 반환해야 합니다.");
        verify(userRepository).existsByEmail(userEmail);

        System.out.println("✅ 이메일 중복확인 테스트 통과!");
        System.out.println("================================================");
    }

    @Test
    void 이메일_중복확인_사용불가능한_경우_false를_반환한다(){
        // Arrange (준비)
        System.out.println("=========이메일 중복확인 사용 불가능한 경우 false를 반환==============");
        String userEmail = "testUser@test.com";
        when(userRepository.existsByEmail(userEmail)).thenReturn(true);

        // Act (실행)
        boolean result = authService.isEmailAvailable(userEmail);

        // Assert (검증)
        assertFalse(result, "사용 불가능한 이메일은 false를 반환해야 합니다.");
        verify(userRepository).existsByEmail(userEmail);

        System.out.println("✅ 이메일 중복확인 테스트 통과!");
        System.out.println("================================================");
    }







}
