package com.example.webapp.controller;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoStats;
import com.example.webapp.entity.ToDo;
import com.example.webapp.service.DashBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {

    private final DashBoardService dashBoardService;


    @Operation(
            summary = "오늘 할 일 리스트 조회 ",
            description = """
                    ## 대시보드 오늘 할 일 조회 API
                    금일자로 예정되어 있는 할 일 리스트를 조회합니다
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/today")
    public ResponseEntity<List<ToDoResponseDTO>> todayTodo(){

        List<ToDoResponseDTO> result = dashBoardService.findTodayToDoList();

        return ResponseEntity.ok(result);
    }


    @Operation(
            summary = "긴급한 할 일 리스트 조회",
            description = """
                    ## 긴급한 할 일 리스트 조회 API
                    긴급한 할 일들을 조회합니다
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/urgent")
    public ResponseEntity<List<ToDoResponseDTO>> todoUrgent(){

        List<ToDoResponseDTO> urgentList = dashBoardService.findCriticalToDoList();

        return ResponseEntity.ok(urgentList);

    }


    @Operation(
            summary = "오늘의 할 일 통계 조회",
            description = """
                    ## 오늘 할 일 진행 상태 조회 API
                    금일 할 일 진행 상태에 대한 정보를 요청합니다
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/stats")
    public ResponseEntity<TodoStats> todoStats(){

        TodoStats result = dashBoardService.getTaskStatus();

        return ResponseEntity.ok(result);

    }


    @Operation(
            summary = "기한이 지난 할 일 리스트 조회",
            description = """
                    ## 기한이 지난 할 일 조회 API
                    마감 기한이 지난 할 일 리스트를 요청합니다
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/overdue")
    public ResponseEntity<List<ToDoResponseDTO>> overdueTodos(){

        List<ToDoResponseDTO> result = dashBoardService.findOverdueToDoList();

        return ResponseEntity.ok(result);
    }

}
