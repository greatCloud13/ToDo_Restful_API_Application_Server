package com.example.webapp.controller;

import com.example.jwt.util.SecurityUtils;
import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoRequestDTO;
import com.example.webapp.DTO.request.BulkUpdateRequest;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.service.ToDoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Tag(name = "ToDo", description = "ToDo 관리 API")
public class ToDoController {

    private final ToDoService toDoService;
    private final SecurityUtils securityUtils;


    @Operation(
            summary = "ToDo 리스트 조회",
            description = """
                    ## ToDo 리스트 조회 API
                    등록한 ToDo 리스트를 조회합니다.
                    - 개발일자:
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    Pageable  pageable
                    """
    )
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<ToDoResponseDTO>> getUserTodos(Pageable pageable){

        User user = securityUtils.getCurrentUserOrThrow();
        Page<ToDoResponseDTO> page = toDoService.findTodoListByUser(user, pageable);

        return ResponseEntity.ok(page);
    }


    @Operation(
            summary = "ToDo 상세조회",
            description = """
                    ## ToDo 상세조회 API
                    요청한 ID의 Todo를 상세조회합니다.
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    id 게시글 ID
                    """
    )
    @GetMapping("/{id}")
    public ResponseEntity<ToDoResponseDTO> detail(@PathVariable Integer id){

        String username = SecurityUtils.getCurrentUsername();

        ToDoResponseDTO result = toDoService.findTodoById(id, username);

        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "ToDo 등록",
            description = """
                    ## ToDo 등록 API
                    요청한 사용자의 ToDo를 등록합니다.
                    - 개발일자:
                    - 수정일자:
                    -테스트 여부:
                    
                    ### 필수 입력 항목
                    TodoRequestDTO
                    """
    )
    @PostMapping("/post")
    public ResponseEntity<ToDo> create(@RequestBody TodoRequestDTO requestDTO){

        User user = securityUtils.getCurrentUserOrThrow();

        ToDo result = toDoService.saveToDo(requestDTO, user);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(result);
    }


    @Operation(
            summary = "ToDo 업데이트",
            description = """
                    ## ToDo 업데이트 API
                    기존의 ToDo를 업데이트 합니다.
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    id : Integer
                    TodoRequestDTO
                    """
    )
    @PatchMapping("/{id}")
    public ResponseEntity<TodoRequestDTO> update(@PathVariable Integer id, @RequestBody TodoRequestDTO requestDTO){

        TodoRequestDTO result = toDoService.UpdateToDo(id, requestDTO);

        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "ToDo 삭제",
            description = """
                    ## ToDo 삭제 API
                    기존의 ToDo를 선택하여 삭제합니다.
                    - 개발일자:
                    - 수정일자:
                    - 테스트 여부:
    
                    ### 필수 입력 항목
                    - id : Integer
                    """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> delete(@PathVariable Integer id){

        boolean result = toDoService.DeleteTodo(id);

        return ResponseEntity.ok().body(result);
    }


    @Operation(
            summary = "ToDo 상태 변경",
            description = """
                    ## ToDo 완료 API
                    선택한 Todo의 상태를 변경합니다.
                    - 개발일자:
                    - 수정일자:
                    - 테스트 여부:
                    """
    )
    @PostMapping("/status/{id}")
    public ResponseEntity<ToDo.TaskStatus> taskDone(@PathVariable Integer id, ToDo.TaskStatus status){

        ToDo.TaskStatus result = toDoService.UpdateStatus(id, status);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "ToDo 상태 일괄 변경",
            description = """
                ## ToDo 상태 일괄 변경 API
                기존의 ToDo의 상태를 일괄적으로 변경합니다.
                - 개발일자: 2025-10-13
                - 수정일자:
                - 테스트 여부:
                """
    )
    @PatchMapping("/bulkUpdate")
    public ResponseEntity<List<ToDoResponseDTO>> bulkUpdateStatus(@RequestBody BulkUpdateRequest request){

        List<ToDoResponseDTO> result = toDoService.bulkUpdate(request);

        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "ToDo 일괄 삭제",
            description = """
                    ## ToDo 일괄 삭제 API
                    요청한 ToDo를 일괄적으로 삭제합니다.
                    - 개발일자:
                    - 수정일자:
                    """
    )
    @DeleteMapping("/bulkDelete")
    public ResponseEntity<Boolean> bulkDelete(@RequestBody List<Long> request){

        boolean result = toDoService.bulkDelete(request);

        return ResponseEntity.ok(result);
    }
}
