package com.example.webapp.repository;

import com.example.webapp.DTO.AnalyticsDistributionDTO;
import com.example.webapp.DTO.CategoryDistribution;
import com.example.webapp.DTO.PriorityDistribution;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<ToDo, Long>, ToDoInsightRepository {

    /**
     * ID를 통해 할 일 조회
     * @param id 할 일 ID
     * @return 할 일 entity (Optional)
     */
    Optional<ToDo> findById(int id);

    /**
     * 사용자를 통해 할 일 조회
     * @param user User entity
     * @return 페이징 된 할 일 리스트
     */
    @Query("SELECT t FROM ToDo t JOIN FETCH t.user WHERE t.user = :user")
    Page<ToDo> findByUser(@Param("user")User user, Pageable pageable);

    /**
     * 시작일자와 종료 예정일자를 기준으로 조회
     * @param startDate 조회 시작일자
     * @param endDate 조회 종료일자
     * @return 할 일 리스트
     */
    List<ToDo> findByUserAndPlanningDateBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 우선 순위 및 상태를 기준으로 조회
     * @param taskPriority 우선순위
     * @return 할 일 리스트
     */
    List<ToDo> findByUserAndTaskPriorityAndStatus(User user, ToDo.TaskPriority taskPriority, ToDo.TaskStatus status);

    /**
     * 요청한 상태에 해당하는 할 일의 갯수를 조회합니다
     * @param taskStatus 할 일 상태
     * @return 해당 상태의 할 일 갯수
     */
    long countByUserAndStatus(User user, ToDo.TaskStatus taskStatus);

    /**
     * 요청한 유저의 할 일을 상태를 조회합니다.
     * @param user 사용자 객체
     * @return 해당 사용자의 할 일 갯수
     */
    long countByUser(User user);

    /**
     * 선택한 일자 이전까지의 전체 할 일 갯수를 조회
     * @param user 사용자 객체
     * @param targetDate 조회 기준
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndPlanningDateBefore(User user, LocalDateTime targetDate);

    /**
     * 선택한 일자, 선택한 상태에 해당하는 할 일의 갯수를 조회
     * @param user 사용자 객체
     * @param targetDate 기준 일자
     * @param status 상태
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndPlanningDateBeforeAndStatus(User user, LocalDateTime targetDate, ToDo.TaskStatus status);

    /**
     * 요청한 유저의 특정 기간 이전 할 일을 조회합니다
     * @param user 사용자 객체
     * @param planningDate 특정 날짜
     * @return 선택한 날짜 이전의 할 일
     */
    List<ToDo> findByUserAndPlanningDateBefore(User user, LocalDateTime planningDate);

    /**
     * 시작일자와 종료일자 에정일자를 기준으로 할 일 갯수 조회
     * @param user 사용자 객체
     * @param startDate 시작일
     * @param endDate 종료일
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndPlanningDateBetween(User user, LocalDate startDate, LocalDate endDate);

    /**
     * 선택한 날짜보다 일정이 지연된 할 일 갯수 조회
     * @param date 선택 일자
     * @param userId 사용자 고유 ID
     * @return 조건에 맞는 할 일 갯수
     */
    @Query("SELECT COUNT(t) FROM ToDo t WHERE t.planningDate > :selectedDate AND t.user.id = :userId")
    long countTodosAfterPlanningDate(@Param("selectedDate") LocalDateTime date, @Param("userId") Long userId);

    /**
     * 선택한 중요도에 해당하는 할 일의 갯수를 조회
     * @param user 사용자 객체
     * @param priority 중요도
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndTaskPriority(User user, ToDo.TaskPriority priority);

    /**
     * 할 일이 생성된 날짜의 갯수를 조회
     * @param userId 사용자 고유 ID
     * @param startDate 검색일자
     * @return 할 일이 생성된 일자 수
     */
    @Query("SELECT COUNT(DISTINCT DATE(t.createdAt)) FROM ToDo t WHERE t.user.id = :userId AND t.createdAt >= :startDate")
    long countActiveDays(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    /**
     * 선택한 일자에 생성된 할 일의 갯수를 조회
     * @param user 사용자 객체
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndCreatedAtBetween(User user, LocalDateTime startDate, LocalDateTime endDate);

    /**
     * 선택한 일자와 상태에 따른 할 일 갯수를 조회
     * @param user 사용자 객체
     * @param startDate 조회 시작일
     * @param endDate 조회 종료일
     * @param status 상태
     * @return 조건에 맞는 할 일 갯수
     */
    long countByUserAndCreatedAtBetweenAndStatus(User user, LocalDateTime startDate, LocalDateTime endDate, ToDo.TaskStatus status);

    /**
     * 선택한 일자부터 현재 일자까지의 우선순위별 분포 정보를 조회
     * @param userId 사용자 고유 ID
     * @param startDate 조회 시작 일자
     * @return Priority 객체 리스트
     */
    @Query("SELECT new com.example.webapp.DTO.PriorityDistribution(t.taskPriority, COUNT(t)) " +
            "FROM ToDo t " +
            "WHERE t.user.id = :userId AND t.createdAt >= :startDate " +
            "GROUP BY t.taskPriority")
    List<PriorityDistribution> countByUserAndTaskPriorityDistribution(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);

    /**
     *  선택한 일자부터 현재 일자까지의 카테고리별 진행 상황을 조회
     * @param userId 사용자 고유 ID
     * @param startDate 조회 시작일자
     * @return CategoryDistribution 객체 리스트
     */
    @Query("SELECT new com.example.webapp.DTO.CategoryDistribution(t.category, " +
            "COUNT(CASE WHEN t.status = 'COMPLETE' THEN 1 ELSE null END), " +
            "COUNT(CASE WHEN t.status = 'IN_PROGRESS' THEN 1 ELSE null END), " +
            "COUNT(CASE WHEN t.status = 'ON_HOLD' THEN 1 ELSE null END)," +
            "0L) " +
            "FROM ToDo t " +
            "WHERE t.user.id = :userId AND t.createdAt >= :startDate " +
            "GROUP BY t.category")
    List<CategoryDistribution> countByUserAndCategoryAndTask(@Param("userId") Long userId, @Param("startDate") LocalDateTime startDate);


}
