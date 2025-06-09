package com.example.webapp.repository;

import com.example.webapp.entity.ToDo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ToDoMapper {
//    toDo 리스트 조회
    List<ToDo> selectAll();

//    toDo 단일 조회
    ToDo selectById(@Param("id") Integer id);

//    toDo 생성
    void insert(ToDo toDo);

//    toDo 업데이트
    void update(ToDo toDo);

//    지정 toDo 삭제
    void delete(@Param("id") Integer id);
}
