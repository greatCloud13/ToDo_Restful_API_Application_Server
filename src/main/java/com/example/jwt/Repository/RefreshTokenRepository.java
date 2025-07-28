package com.example.jwt.Repository;

import com.example.webapp.entity.RefreshToken;
import com.example.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * RefreshToken Entity 대한 데이터 접근 계층
 * Refresh Token의 생성, 조회, 삭제 등을 담당
 */
@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    /**
     * 토큰 문자열로 RefreshToken 조회
     * @param token 토큰 문자열
     * @return RefreshToken 엔티티 (Optional)
     */
    Optional<RefreshToken> findByToken(String token);

    /**
     * 사용자별 RefreshToken 조회
     *
     * @param user 사용자 Entity
     * @return RefreshToken 리스트
     */
    List<RefreshToken> findByUser(User user);

    /**
     * 사용자의 활성 RefreshToken 조회 (만료되지 않은 토큰)
     *
     * @param user 사용자 Entity
     * @param currentTime 현재 시간
     * @return 활성 Refresh Token 리스트
     */
    List<RefreshToken> findByUserAndExpiryDateAfter(User user, LocalDateTime currentTime);

    /**
     * 사용자의 활성 RefreshToken 개수 조회
     *
     * @param user 사용자 Entity
     * @param currentTime 현재 시간
     * @return 활성 토큰 개수
     */
    long countByUserAndExpiryDateAfter(User user, LocalDateTime currentTime);

    /**
     * 사용자별 RefreshToken 삭제
     *
     * @param user 사용자 엔티티
     * @return 삭제된 토큰 개수
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.user=:user")
    int deleteByUser(@Param("user") User user);

    /**
     * 특정 토큰 삭제
     *
     * @param token 토큰 문자열
     * @return 삭제된 토큰 갯수
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.token = :token")
    int deleteByToken(@Param("token") String token);

    /**
     * 만료된 Refresh Token 삭제
     *
     * @param currentTime 현재 시간
     * @return 삭제된 토큰 갯수
     */
    @Modifying
    @Query("DELETE FROM RefreshToken rt WHERE rt.expiryDate < :currentTime")
    int deleteByExpiryDateBefore(@Param("currentTime") LocalDateTime currentTime);





}
