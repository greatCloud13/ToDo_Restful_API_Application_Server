package com.example.webapp.service.impl;

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
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ToDoServiceImpl implements ToDoService {

    private final TodoRepository todoRepository;

    @Override
    public ToDo findTodoById(int id) {
        log.info("할 일 조회 고유 id: {}",id);

        return todoRepository.findById(id)
                .orElseThrow(()->new EntityNotFoundException(
                        "Not Found"
                ));
    }


    @Override
    public Page<ToDo> findTodoListByUser(User user, Pageable pageable) {
        return todoRepository.findByUser(user, pageable);
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
}
