package com.example.webapp.service;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoRequestDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ToDoService {

    /**
     * id를 통해 할 일 상세조회
     * @param id 할 일 고유 id
     * @return ToDo 객체
     */
    public ToDoResponseDTO findTodoById(int id, String username);

    /**
     * User를 통해 할 일 리스트 조회
     * @param user 사용자
     * @param pageable pageable 객체
     * @return 할 일 리스트
     */
    public Page<ToDoResponseDTO> findTodoListByUser(User user, Pageable pageable);

    /**
     * 할 일 등록
     * @param toDoDTO 할 일 객체
     * @return 등록된 할 일
     */
    public ToDo saveToDo(TodoRequestDTO toDoDTO, User user);

    /**
     * 할 일 수정
     * @param id 할 일 고유 id
     * @param updateToDo 수정할 할 일 객체
     * @return 수정된 객체
     */
    public TodoRequestDTO UpdateToDo(int id, TodoRequestDTO updateToDo);

    /**
     * 할 일 상태 변경
     * @param id 할 일 고유 id
     * @param status 상태
     * @return 수정된 객체
     */
    public ToDo.TaskStatus UpdateStatus(int id, ToDo.TaskStatus status);

    /**
     * 할 일 삭제
     * @param id 할 일 고유 id
     * @return 삭제 여부
     */
    public Boolean DeleteTodo(int id);

    /**
     * 할 일 작성자 검증
     * @param user 사용자 객체
     * @return 접근 가능 여부
     */
    public Boolean CheckOwner(int id, String username);

}
