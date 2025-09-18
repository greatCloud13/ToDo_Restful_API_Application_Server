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
     * 우선순위를 선택하여 조회
     * @param taskPriority 우선순위
     * @return 할 일 리스트
     */
    List<ToDo> findByUserAndTaskPriority(User user, ToDo.TaskPriority taskPriority);


}
