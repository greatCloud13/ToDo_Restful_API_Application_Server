package com.example.webapp.service.impl;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoStats;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.DashBoardService;
import jakarta.persistence.Table;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
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
        log.info("금일 종료 예정 TodoList 조회 사용자명: {}",username);

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

    @Override
    @Transactional
    public List<ToDoResponseDTO>findCriticalToDoList(){

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("매우 급한 ToList 조회 사용자명: {}",username);

        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new AuthorizationDeniedException(
                        "사용자를 찾을 수 없습니다."
                ));

        return todoRepository.findByUserAndTaskPriorityAndStatus(user, ToDo.TaskPriority.VERY_HIGH, ToDo.TaskStatus.IN_PROGRESS)
                .stream()
                .map(ToDoResponseDTO :: from)
                .toList();


    }

    @Override
    @InjectUserEntity
    @Transactional(readOnly = true)
    public TodoStats  getTaskStatus() {

        User user = UserContext.getCurrentUser();
        TodoStats stats = new TodoStats();

        long total = todoRepository.countByUser(user);
        log.info("사용자: {} 전체 ToDo 갯수 조회: {}", user.getUsername(), total);

        long completed = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.COMPLETE);
        log.info("사용자: {} 완료된 ToDo 갯수 조회: {}", user.getUsername(), completed);

        long inProgress = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.IN_PROGRESS);
        log.info("사용자: {} 진행중 ToDo 갯수 조회: {}", user.getUsername(), inProgress);

        long pending = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.ON_HOLD);
        log.info("사용자: {} 보류중 ToDo 갯수 조회: {}", user.getUsername(), pending);

        long completeRate = 0;
        if(total > 0){
            completeRate = Math.round((double) completed * 100 / total);
        }


        stats.setTotal(total);
        stats.setCompleted(completed);
        stats.setInprogress(inProgress);
        stats.setPending(pending);
        stats.setCompletionRate(completeRate);

        return stats;
    }

    @Override
    @InjectUserEntity
    @Transactional
    public List<ToDoResponseDTO> findOverdueToDoList() {

        User user = UserContext.getCurrentUser();
        LocalDateTime now = LocalDate.now().minusDays(1).atTime(LocalTime.MAX);

        List<ToDo> result = todoRepository.findByUserAndPlanningDateBefore(user, now);

        return result.stream().map(ToDoResponseDTO :: from).toList();
    }


}
