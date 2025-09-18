package com.example.webapp.service.impl;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.DashBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class DashBoardServiceImpl implements DashBoardService {

    private final TodoRepository todoRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public List<ToDoResponseDTO> findTodayToDoList() {

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("금일 종료 예정 ToList 조회 사용자명: {}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new AuthorizationDeniedException(
                        "사용자를 찾을 수 없습니다."
                ));

//      검색 시작일자
        LocalDateTime startDate = LocalDate.now().atStartOfDay();

//      종료일자
        LocalDateTime endDate = LocalDate.now().atTime(LocalTime.MAX);

        List<ToDo> result = todoRepository.findByUserAndPlanningDateBetween(user, startDate, endDate);

//      ToDoEntity를 ResponseDTO로 매핑
        return result.stream()
                .map(ToDoResponseDTO :: from)
                .toList();
    }
}
