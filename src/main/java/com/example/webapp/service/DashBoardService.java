package com.example.webapp.service;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.entity.ToDo;

import java.util.List;

public interface DashBoardService {

    /**
     * 오늘 까지 종료되어야 하는 할 일을 제공
     * @return 오늘까지 종료되어야 하는 할 일 리스트
     */
    List<ToDoResponseDTO> findTodayToDoList();


}
