package com.example.webapp.repository;

import com.example.webapp.DTO.BestProducibilityDateDTO;
import com.example.webapp.DTO.WarnInsightDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;

import java.time.LocalDateTime;
import java.util.List;

/**
 * ToDoRepository QueryDSL 인터페이스
 */
public interface ToDoInsightRepository {

    BestProducibilityDateDTO getBestProducibilityDate(User user);

    List<WarnInsightDTO> getWarnInsight(User user);

}
