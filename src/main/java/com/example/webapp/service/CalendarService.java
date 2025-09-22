package com.example.webapp.service;

import com.example.webapp.DTO.ToDoResponseDTO;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CalendarService {

    List<ToDoResponseDTO> getThisMonthTodo(LocalDate startDate, LocalDate lastDate);

}
