package com.example.webapp.service;

import com.example.webapp.DTO.TodoRequestDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ToDoService {

    /**
     * id를 통해 할일 상세조회
     * @param id 할 일 고유 id
     * @return ToDo 객체
     */
    public ToDo findTodoById(int id);

    /**
     * User를 통해 할일 리스트 조회
     * @param user 사용자
     * @param pageable pageable 객체
     * @return 할 일 리스트
     */
    public Page<ToDo> findTodoListByUser(User user, Pageable pageable);

    /**
     * 할 일 등록
     * @param toDoDTO 할 일객체
     * @return 등록된 할 일
     */
    public TodoRequestDTO saveToDo(TodoRequestDTO toDoDTO, User user);



}
