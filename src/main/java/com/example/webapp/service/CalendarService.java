package com.example.webapp.service;

import com.example.webapp.DTO.ToDoResponseDTO;

import java.util.List;

public interface CalendarService {

    List<ToDoResponseDTO> getThisMonthTodo();

}
