package com.example.webapp.repository;

import com.example.webapp.entity.Qna;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnaRepository extends JpaRepository<Qna, Long> {

    @Query("SELECT q FROM Qna q JOIN FETCH q.owner WHERE q.owner = :owner")
    Page<Qna> findAllByOwner(@Param("owner") User owner, Pageable pageable);

    @Query("SELECT q FROM Qna q JOIN FETCH q.owner WHERE q.id = :id")
    Optional<Qna> findByIdWithOwner(@Param("id")Long id);

}
