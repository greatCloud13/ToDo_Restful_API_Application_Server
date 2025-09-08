package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

/**
 * 사용자 Entity
 * JWT 인증 시스템에서 사용되는 정보
 */
@Entity
@Table (name = "users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "password") //비밀번호는 TtoString에서 제외
public class User {

    /**
     * 사용자 고유 ID(Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    /**
     * 사용자명 (로그인 ID)
     * 3~20자 영문숫자밑줄만 허용
     */
    @Column(name = "username", nullable = false, unique = true, length = 20)
    private String username;

    /**
     * 이메일 주소
     */
    @Column(name = "email", nullable = false, unique = true, length = 100)
    private String email;

    /**
     * 암호화된 비밀번호
     */
    @Column(name = "password", nullable = false)
    private String password;

    /**
     * 사용자 역할 (권한)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    @Builder.Default
    private Role role = Role.USER;

    /**
     * 계정 활성화 여부
     */
    @Column(name = "enabled", nullable = false)
    @Builder.Default
    private boolean enabled = true;

    /**
     * 엔티티 생성 시간
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable =false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 엔티티 수정 시간
     */
    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * 활성 여부 반환 (Spring Security 연동)
     *
     * @return 활성 상태 여부
     */
    public boolean isActive(){
        return enabled;
    }

    /**
     * 관리자 여부 확인
     *
     * @return 관리자 여부
     */
    public boolean isAdmin(){
        return role.isAdmin();
    }

    /**
     * 사용자 여부 확인
     *
     * @return 사용자 여부
     */
    public boolean isUser(){
        return role.isUser();
    }

    /**
     * 비밀번호 변경
     *
     * @param newPassword 새 비밀번호 (crypted)
     */
    public void changePassword(String newPassword){
        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        this.updatedAt = LocalDateTime.now();
    }
}
