package com.example.jwt.Service;

import com.example.jwt.Repository.RefreshTokenRepository;
import com.example.jwt.Repository.UserRepository;
import com.example.jwt.config.JwtProperties;
import com.example.jwt.exception.InvalidJwtTokenException;
import com.example.webapp.entity.RefreshToken;
import com.example.webapp.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * Refresh Token 을 데이터베이스에서 관리하는 서비스
 * 토큰 생성, 검증, 갱신, 삭제 등의 기능을 제공
 */

@Slf4j
@Service
@RequiredArgsConstructor
public class RefreshTokenService {

    private final RefreshTokenRepository  refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties properties;

    /**
     * 새로운 Refresh Token 생성
     *
     * @param userId 사용자 ID
     * @return 생성된 RefreshToken 엔티티
     */
    @Transactional
    public RefreshToken createRefreshToken(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()-> new InvalidJwtTokenException("사용자를 찾을 수 없습니다 :"+ userId));

        // 기존 Refresh Token 있다면 삭제 (하나의 사용자당 하나의 토큰만 배치)
        refreshTokenRepository.deleteByUser(user);

        // 새로운 Refresh Token 생성
        RefreshToken refreshToken = RefreshToken.builder()
                .user(user)
                .token(UUID.randomUUID().toString())
                .expiryDate(LocalDateTime.now().plusSeconds(properties.getRefreshTokenExpiration()/1000))
                .build();

        RefreshToken saveToken = refreshTokenRepository.save(refreshToken);
        log.debug("사용자 {}에 대한 새 Refresh Token 생성: {}", userId, saveToken.getToken());

        return saveToken;
    }

    /**
     * 사용자명으로 Refresh Token 삭제 (로그아웃)
     *
     * @param username 사용자명
     */
    @Transactional
    public void deleteByUsername(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new InvalidJwtTokenException("사용자를 찾을 수 없습니다"+username));

        deleteByUserId(user.getId());
    }

    /**
     * 특정 토큰 삭제
     *
     * @param token Refresh Token 문자열
     */
    @Transactional
    public void deleteByToken(String token){
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(()->new InvalidJwtTokenException("Refresh Token 을 찾을 수 없습니다:"+token));

        refreshTokenRepository.delete(refreshToken);
        log.debug("Refresh Token 삭제 완료:{}",token);
    }

    /**
     * 만료된 Refresh Token 정리 (스케쥴링 용도)
     *
     * @return 삭제된 토큰 갯수
     */
    @Transactional
    public int deleteExpiredTokens(){
        LocalDateTime now = LocalDateTime.now();
        int deletedCount = refreshTokenRepository.deleteByExpiryDateBefore(now);

        if(deletedCount>0){
            log.info("만료된 Refresh Token{}개 정리 완료", deletedCount);
        }
        else {
            log.info("삭제된 토큰이 없습니다");
        }

        return deletedCount;
    }

    /**
     * 사용자의 활성 Refresh Token 개수 조회
     *
     * @param userId 사용자 ID
     * @return 활성 토큰 개수
     */
    @Transactional(readOnly = true)
    public long countActiveTokensByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new InvalidJwtTokenException("사용자를 찾을 수 없습니다:"+userId));

        return refreshTokenRepository.countByUserAndExpiryDateAfter(user, LocalDateTime.now());
    }

    /**
     * 특정 사용자의 모든 활성 Refresh Token 조회
     *
     * @param userId 사용자 ID
     * @return 활성 RefreshToken 리스트
     */
    @Transactional
    public java.util.List<RefreshToken> findActiveTokenByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new InvalidJwtTokenException("사용자를 찾을 수 없습니다: "+userId));

        return refreshTokenRepository.findByUserAndExpiryDateAfter(user, LocalDateTime.now());
    }

    /**
     * 사용자명을 통해 Refresh Token 생성
     *
     * @param username 사용자명
     * @return 생성된 RefreshToken 엔티티
     */
    @Transactional
    public RefreshToken createRefreshToken(String username){
        User user = userRepository.findByUsername(username)
                .orElseThrow(()->new InvalidJwtTokenException("사용자를 찾을 수 없습니다: "+username));

        return createRefreshToken(user.getId());
    }

    /**
     * Refresh Token 으로 토큰 정보 조회
     *
     * @param token Refresh Token 문자열
     * @return RefreshToken 엔티티(Optional)
     */
    @Transactional(readOnly = true)
    public Optional<RefreshToken> findByToken(String token){
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Refresh Token 유효성 검증
     *
     *
     * @param refreshToken Refresh Token 엔티티
     * @return 검증된 Refresh Token 엔티티
     * @throws InvalidJwtTokenException 토큰이 만료된 경우
     */
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken refreshToken){
        if(refreshToken.getExpiryDate().isBefore(LocalDateTime.now())){
            log.warn("만료된 Refresh Token 삭제: {}", refreshToken.getToken());
            refreshTokenRepository.delete(refreshToken);
            throw new InvalidJwtTokenException("Refresh Token 만료되었습니다. 다시 로그인해주세요");
        }

        log.debug("Refresh Token 검증 성공: {}", refreshToken.getToken());
        return refreshToken;
    }

    /**
     * 사용자의 Refresh Token 삭제(로그아웃)
     *
     * @param userId 사용자 ID
     */
    @Transactional
    public void deleteByUserId(Long userId){
        User user = userRepository.findById(userId)
                .orElseThrow(()->new InvalidJwtTokenException("사용자를 찾을 수 없습니다: "+userId));

        int deleteCount = refreshTokenRepository.deleteByUser(user);
        log.debug("사용자 {}의 Refresh Token {} 개 삭제 완료", userId, deleteCount);
    }
}
