package com.example.webapp.service.impl;


import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {

    private final TodoRepository todoRepository;


    @Override
    @InjectUserEntity
    @Transactional(readOnly = true)
    public List<ToDoResponseDTO> getThisMonthTodo() {

        User user = UserContext.getCurrentUser();

        LocalDate now = LocalDate.now();

        LocalDateTime startDate = now.withDayOfMonth(1).atTime(LocalTime.MIN);
        LocalDateTime lastDate = now.withDayOfMonth(now.lengthOfMonth()).atTime(LocalTime.MAX);

        List<ToDo> result = todoRepository.findByUserAndPlanningDateBetween(user, startDate, lastDate);

        return result.stream().map(ToDoResponseDTO :: from).toList();
    }

}
