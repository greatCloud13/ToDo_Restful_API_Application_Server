package com.example.jwt.Repository;

import com.example.webapp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * User Entity에 대한 데이터 접근 계층
 * String Data JPA 이용한 Repository 인터페이스
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * 사용자명으로 사용자 조회
     *
     * @param username 사용자 명
     * @return User 엔티티 (Optional)
     */
    Optional<User> findByUsername(String username);

    /**
     * 사용자 이메일로 조회
     *
     * @param email 이메일
     * @return User 엔티티 (Optional)
     */
    Optional<User> findByEmail(String email);

    /**
     * 사용자명 중복 확인
     *
     * @param username 사용자명
     * @return 존재 여부
     */
    boolean existsByUsername(String username);

    /**
     * 이메일 중복 확인
     *
     * @param email 이메일
     * @return 존재 여부
     */
    boolean existsByEmail(String email);

    /**
     * 활성화된 사용자 목록 조회
     *
     * @return 활성화 되어있는 사용자 목록 리스트
     */
    List<User> findByEnabledTrue();

    /**
     * 비활성화된 사용자 목록 조회
     *
     * @return 비활성화 되어있는 사용자 리스트
     */
    List<User> findByEnabledFalse();

    /**
     * 역할별 사용자 조회
     *
     * @param role 사용자 역할
     * @return 해당 역할의 사용자 리스트
     */
    List<User> findByRole(com.example.webapp.entity.Role role);

    /**
     * 생성일 기준 사용자 조회 (특정 날짜 이후)
     *
     * @param dateTime 기준 날짜
     * @return 기준 날짜 이후 생성된 사용자 리스트
     */
    List<User> findByCreatedAtAfter(LocalDateTime dateTime);

    /**
     * 특정 기간 생성 사용자 조회
     *
     * @param startDate 시작 날짜
     * @param endDate 종료 날짜
     * @return 해당 기간에 생성된 사용자 리스트
     */
    List<User> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 사용자명과 이메일로 사용자 조회 (비밀번호 찾기용)
     *
     * @param username 사용자명
     * @param email 이메일
     * @return User 엔티티 (Optional)
     */
    Optional<User> findByUsernameAndEmail(String username, String email);

    /**
     * 사용자 명 또는 이메일로 사용자 조회 (로그인 시 유연한 검색)
     *
     * @param identifier 아이디
     * @return User 엔티티 (Optional)
     */
    @Query("SELECT u FROM User u.user = :identifier OR u.email =identifier")
    Optional<User> findByUsernameOrEmail(@Param("identifier") String identifier);

    /**
     * 활성화 되어있는 사용자 중 사용자 명으로 조회
     *
     * @param username 사용자명
     * @return User 엔티티 (Optional)
     */
    Optional<User> findByUsernameAndEnabledTrue(String username);

    /**
     * 특정 역할 사용자 수 조회
     *
     * @param role 사용자 역할
     * @return 사용자 수
     */
    long countByRoleAndEnabledTrue(com.example.webapp.entity.Role role);





}
