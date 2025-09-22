package com.example.webapp.controller;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.service.CalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/calendar")
@RequiredArgsConstructor
public class CalendarController {

    private final CalendarService calendarService;

    @GetMapping("/month")
    public ResponseEntity<List<ToDoResponseDTO>> thisMonthTodo(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate, @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate lastDate){

        List<ToDoResponseDTO> todoList = calendarService.getThisMonthTodo(startDate, lastDate);

        return ResponseEntity.ok(todoList);
    }


}
