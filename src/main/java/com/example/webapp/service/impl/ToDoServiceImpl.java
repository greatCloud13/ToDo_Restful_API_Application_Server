package com.example.webapp.service.impl;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoRequestDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.ToDoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToDoServiceImpl implements ToDoService {

    private final TodoRepository todoRepository;

    @Override
    public ToDoResponseDTO findTodoById(int id, String username) {
        log.info("할 일 조회 고유 id: {}",id);

        ToDo todo = todoRepository.findById(id)
                .orElseThrow(()->new AccessDeniedException(
                        "권한이 없거나 존재하지 않는 게시물입니다."
                ));


        return ToDoResponseDTO.from(todo);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ToDoResponseDTO> findTodoListByUser(User user, Pageable pageable) {

        Page<ToDo> result = todoRepository.findByUser(user, pageable);

        return result.map(ToDoResponseDTO :: from);
    }

    @Override
    public ToDo saveToDo(TodoRequestDTO toDoDTO, User user) {

        ToDo todo = ToDo.builder()
                .title(toDoDTO.getTitle())
                .memo(toDoDTO.getMemo())
                .category(toDoDTO.getCategory())
                .taskPriority(toDoDTO.getPriority())
                .planningDate(toDoDTO.getTargetDate())
                .status(toDoDTO.getStatus())
                .user(user)
                .build();

        return todoRepository.save(todo);
    }

    @Override
    public TodoRequestDTO UpdateToDo(int id, TodoRequestDTO updateToDo) {

        ToDo todo = todoRepository.findById(id).orElseThrow(()->new EntityNotFoundException(
                "Todo Not Found"));

        todo.setTitle(updateToDo.getTitle());
        todo.setTaskPriority(updateToDo.getPriority());
        todo.setCategory(updateToDo.getCategory());
        todo.setMemo(updateToDo.getMemo());
        todo.setStatus(updateToDo.getStatus());
        todo.setPlanningDate(updateToDo.getTargetDate());

        todoRepository.save(todo);
        return updateToDo;
    }

    @Override
    public ToDo.TaskStatus UpdateStatus(int id, ToDo.TaskStatus status) {

        ToDo todo = todoRepository.findById(id)
                .orElseThrow(
                        ()-> new EntityNotFoundException(
                                "Not Found"
                        )
                );

        todo.setStatus(status);

        if(status.equals(ToDo.TaskStatus.COMPLETE)){
            todo.setDoneAt(LocalDateTime.now());
        }
        if(status.equals(ToDo.TaskStatus.IN_PROGRESS)){
            todo.setDoneAt(null);
        }
        if(status.equals(ToDo.TaskStatus.ON_HOLD)){
            todo.setDoneAt(null);
        }

        ToDo result = todoRepository.save(todo);

        return result.getStatus();
    }

    @Override
    public Boolean DeleteTodo(int id) {

        ToDo todo = todoRepository.findById(id).orElseThrow(
                ()-> new EntityNotFoundException(
                        "Not Found"
                ));

        todoRepository.delete(todo);

        return true;
    }

    @Override
    public Boolean CheckOwner(int id, String username) {

        ToDo todo = todoRepository.findById(id).orElseThrow(
                ()->new EntityNotFoundException(
                        "Not Found"
                ));

        return username.equals(todo.getUser().getUsername());
    }


}
