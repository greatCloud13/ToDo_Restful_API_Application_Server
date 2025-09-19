package com.example.webapp.service;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoStats;
import com.example.webapp.entity.ToDo;

import java.util.List;

public interface DashBoardService {

    /**
     * 오늘 까지 종료되어야 하는 할 일을 제공
     * @return 오늘까지 종료되어야 하는 할 일 리스트
     */
    List<ToDoResponseDTO> findTodayToDoList();

    /**
     * 매우 중요한 할 일 리스트 제공
     * @return 매우 중요한 할 일 리스트
     */
    List<ToDoResponseDTO> findCriticalToDoList();

    /**
     * 해당하는 상태의 할 일의 갯수를 제공
     * @return 해당 상태의 할 일 갯수
     */
    TodoStats getTaskStatus();
}
