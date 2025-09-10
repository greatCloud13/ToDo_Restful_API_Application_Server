package com.example.webapp.repository;

import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

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
    Page<ToDo> findByUser(User user, Pageable pageable);
}
