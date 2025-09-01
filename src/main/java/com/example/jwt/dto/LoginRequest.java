package com.example.jwt.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Login Request DTO
 * 사용자 로그인 시 클라이언트에서 전송하는 데이터
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    /**
     * 사용자명 (로그인에 사용하는 ID)
     */
    @NotBlank(message = "사용자명은 필수입니다.")
    @Size(min = 3, max = 20, message = "사용자명은 3~20자 입니다")
    private String username;

    /**
     * 비밀번호
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 4, max = 100, message = "비밀번호는 4~100자 사이여야 합니다.")
    private String password;

}
