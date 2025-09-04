package com.example.webapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/** RefreshToken 엔티티
 *
  */
@Entity
@Table(name = "refresh_Token_tokens",
    indexes = {
        @Index(name="idx_refresh_token_token", columnList = "token"),
        @Index(name="idx_refresh_token_user_id", columnList = "user_id"),
        @Index(name="idx_refresh_token_expiry_date", columnList = "expiry_date")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "token") // 보안상 토큰 값은 로그에서 제외
public class RefreshToken {

    /**
     * Refresh Token 고유 ID (PK)
     */
    @Id
    @GeneratedValue
    @Column(name = "id")
    private long id;

    /**
     * 토큰 문자열
     */
    @Column(name = "token", nullable = false, unique = true, length = 500)
    private String token;

    /**
     * 토큰 소유자 (사용자)
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false,
                foreignKey = @ForeignKey(name = "fk_refresh_token_user"))
    private User user;

    /**
     * 토큰 만료 시간
     */
    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    /**
     * 토큰 생성 시간
     */
    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * 토큰 사용 횟수
     * 보안 모니터링 및 이상 탐지에 활용
     */
    @Column(name = "usage_count", nullable = false)
    @Builder.Default
    private int usageCount = 0;

    /**
     * 마지막 사용 시간
     * 토큰 사용 추적 및 보안 모니터링
     */
    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    /**
     * 토큰 생성 시 사용자 IP 주소
     * 보안 로그 및 이상 접근 탐지에 활용
     */
    @Column(name = "created_ip", length = 45)
    private String createdIp;

    @Column(name = "user_agent", length = 500)
    private String userAgent;

    /**
     * 토큰 활성화 여부
     * 강제 로그아웃, 보안 위한 등으로 토큰을 비활성화할 때 사용
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private boolean isActive = true;

    /**
     * 토큰 폐기 시간
     * 토큰이 명시적으로 폐기된 시간 (로그아웃 등)
     */
    @Column(name="revoked_at")
    private LocalDateTime revokedAt;

    /**
     * 토큰 폐기 사유
     * 로그아웃, 보안위반, 만료 등의 사유 기록
     */
    @Column(name = "revoked_reason", length = 100)
    private String revokedReason;


    //========= 비즈니스 메소드 ================


    /**
     * 만료 여부 확인
     *
     * @return 만료 여부
     */
    public boolean isExpired(){
        return LocalDateTime.now().isAfter(this.expiryDate);
    }

    /**
     * 유효성 확인
     * 만료되지 않고 활성 상태인지 확인
     *
     * @return 유효 여부
     */
    public boolean isValid(){
        return isActive && !isExpired() && revokedAt == null;
    }

    /**
     * 토큰 사용 기록용도
     * 사용 횟수 증가 및 마지막 사용 시간 업데이트
     */
    public void markAsUsed(){
        this.usageCount++;
        this.lastUsedAt = LocalDateTime.now();
    }

    /**
     * 토큰 폐기
     *
     * @param reason 폐기 사유
     */
    public void revoke(String reason){
        this.isActive = false;
        this.revokedAt = LocalDateTime.now();
        this.revokedReason = reason;
    }

    /**
     * 토큰 비활성화
     * 임시적으로 토큰 사용 못하게 함
     */
    public void deactivate(){
        this.isActive = false;
    }

    /**
     * 토큰 재활성화
     * 비활성화된 토큰을 다시 사용할 수 있게 함(만료되지 않은 경우에만)
     */
    public void reactive(){
        if(!isExpired() && revokedAt == null){
            this.isActive = true;
        }
    }

    /**
     * 토큰 유효 기간 확인
     * 단위: 분
     */
    public long getRemainingValidityInMinutes(){
        if(isExpired()){
            return 0;
        }
        LocalDateTime now = LocalDateTime.now();
        return java.time.Duration.between(now, expiryDate).toMinutes();
    }

    /**
     * 토큰이 곧 만료 되는지 확인 (1시간)
     *
     * @return 토큰 유효까지 남은 시간이 1시간 이내 인지
     */
    public boolean isExpiringSoon(){
        return getRemainingValidityInMinutes() <= 60;
    }

    /**
     * 토큰 사용자와 요청의 사용자와 동일한지 확인
     *
     * @param userId 요청 사용자
     * @return 동일 사용자 여부
     */
    public boolean belongToUser(Long userId){
        return this.user != null && this.user.getId().equals(userId);
    }



}
