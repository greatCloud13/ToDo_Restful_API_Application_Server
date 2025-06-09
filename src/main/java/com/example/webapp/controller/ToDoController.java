package com.example.webapp.controller;

import com.example.webapp.entity.ApiResponse;
import com.example.webapp.entity.ToDo;
import com.example.webapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
public class ToDoController {
//    DI
    private final ToDoService toDoService;

    @GetMapping
    public List<ToDo> list(){
        return toDoService.findAllToDo();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ToDo> detail(@PathVariable Integer id){
        ToDo toDo = toDoService.findByIdToDo(id);
        if(toDo != null){
            return ResponseEntity.ok(toDo);
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/post")
    public ResponseEntity<ApiResponse> create (@RequestBody ToDo toDo){
        toDoService.insertToDo(toDo);
        ApiResponse response = new ApiResponse(true, "success", toDo );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/post")
    public ResponseEntity<ToDo> update (@RequestBody ToDo toDo){
        toDoService.updateToDo(toDo);
        return ResponseEntity.ok(toDo);
    }
}
