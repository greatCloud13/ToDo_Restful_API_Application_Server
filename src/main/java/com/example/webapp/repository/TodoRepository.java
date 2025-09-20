package com.example.webapp.repository;

import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TodoRepository extends JpaRepository<ToDo, Long> {

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
    Long countByUserAndStatus(User user, ToDo.TaskStatus taskStatus);

    /**
     * 요청한 유저의 할 일을 상태를 조회합니다.
     * @param user 사용자 객체
     * @return 해당 사용자의 할 일 갯수
     */
    Long countByUser(User user);

    /**
     * 요청한 유저의 특정 기간 이전 할 일을 조회합니다
     * @param user 사용자 객체
     * @param planningDate 특정 날짜
     * @return 선택한 날짜 이전의 할 일
     */
    List<ToDo> findByUserAndPlanningDateBefore(User user, LocalDateTime planningDate);

}
