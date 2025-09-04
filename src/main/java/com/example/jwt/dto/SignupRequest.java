package com.example.jwt.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 회원가입 요청 DTO
 * 새로운 사용자 등록 시 클라이언트에서 전송하는 데이터
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignupRequest {

    /**
     * 사용자 ID
     * 영문, 숫자, _만 허용
     */
    @NotBlank(message = "사용자명은 필수입니다.")
    @Size(min = 3, max = 20, message = "사용자명은 3~20자 사이여야 합니다.")
    @Pattern(regexp = "[a-zA-Z0-9_]+$", message = "사용자명은 영문, 숫자, _만 사용이 가능합니다.")
    private String username;

    /**
     * 이메일 주소
     */
    @NotBlank(message = "이메일은 필수입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    @Size(max = 100, message = "이메일은 100자 이하여야 합니다.")
    private String email;

    /**
     * 비밀번호
     * 최소 8자, 영문 대소문자, 숫자, 특수문자 포함
     */
    @NotBlank(message = "비밀번호는 필수입니다.")
    @Size(min = 8, max = 100, message = "비밀번호는 8-100자 사이여야 합니다.")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
            message = "비밀번호는 영문 대소문자, 숫자, 특수문자를 모두 포함해야 합니다.")
    private String password;


    /**
     * 초대코드
     * 비공개 서비스이므로 개발자에게 문의
     */
    @NotBlank(message = "비공개 코드는 관리자에게 문의하세요.")
    public String inviteCode;

    /**
     * 비밀번호 확인
     */
    @NotBlank(message = "비밀번호 확인은 필수입니다.")
    private String confirmPassword;

    /**
     * 비밀번호와 비밀번호 확인이 일치하는지 검증
     *
     * @return 일치 여부
     */
    public boolean isPasswordMatching() {
        return password != null && password.equals(confirmPassword);
    }



}
