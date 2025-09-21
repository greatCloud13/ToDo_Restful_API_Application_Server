package com.example.webapp.controller;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.service.CalendarService;
import com.example.webapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/month")
    public ResponseEntity<List<ToDoResponseDTO>> thisMonthTodo(){

        List<ToDoResponseDTO> todoList = calendarService.getThisMonthTodo();

        return ResponseEntity.ok(todoList);
    }


}
