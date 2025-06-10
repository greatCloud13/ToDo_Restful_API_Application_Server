package com.example.webapp.controller;

import com.example.webapp.DTO.ToDoDTO;
import com.example.webapp.entity.ApiResponse;
import com.example.webapp.entity.ToDo;
import com.example.webapp.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Tag(name = "ToDo", description = "ToDo 관리 API")
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

    @Operation(
            summary = "ToDo 업데이트",
            description = """
                    ## ToDo 업데이트 API
                    기존의 ToDo를 업데이트 합니다.
                    - 개발일자: 2025-06-10
                    - 수정일자: .
                    - 테스트 여부: 미실시
                    
                    ### 필수 입력 항목
                    - todo : 최대 50자
                    - detail : 제한없음
                    
                    """
    )
    @PatchMapping("/{id}")
    public ResponseEntity<ToDoDTO> update (@PathVariable Integer id, @RequestBody ToDoDTO toDo){
        toDoService.updateToDo(id, toDo);
        return ResponseEntity.ok(toDo);
    }

    @Operation(
        summary = "ToDo 삭제",
        description = """
                ## ToDo 삭제 API
                기존의 ToDo를 선택하여 삭제합니다.
                - 개발일자: 2025-06-10
                - 수정일자: .
                - 테스트 여부: 미실시
                
                ### 필수 입력 항목
                - id : Integer
                """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id){
        toDoService.deleteToDo(id);
        return ResponseEntity.ok(true);
    }
}
