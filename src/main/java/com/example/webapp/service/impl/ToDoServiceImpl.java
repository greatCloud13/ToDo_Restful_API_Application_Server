package com.example.webapp.service.impl;

import com.example.webapp.DTO.ToDoDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.repository.ToDoMapper;
import com.example.webapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// ToDo Service 구현
@Service
@Transactional
@RequiredArgsConstructor
public class ToDoServiceImpl implements ToDoService {

//    DI
    private final ToDoMapper toDoMapper;

    @Override
    public List<ToDo> findAllToDo() {
        return toDoMapper.selectAll();
    }

    @Override
    public ToDo findByIdToDo(Integer id) {
        return toDoMapper.selectById(id);
    }

    public void insertToDo(ToDo toDo){
        toDoMapper.insert(toDo);
    }

    @Override
    public void updateToDo(Integer id, ToDoDTO toDoDTO) {
        ToDo toDo = ToDo.builder()
                .id(id)
                .todo(toDoDTO.getTodo())
                .detail(toDoDTO.getDetail())
                .build();

        toDoMapper.update(toDo);
    }

    @Override
    public void deleteToDo(Integer id) {
        toDoMapper.delete(id);

    }
}
