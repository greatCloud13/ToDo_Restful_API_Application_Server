package com.example.webapp.controller;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.DTO.TodoStats;
import com.example.webapp.entity.ToDo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.function.ToDoubleFunction;

@RestController
@RequestMapping("/dashboard")
@RequiredArgsConstructor
public class DashboardController {


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

        List<ToDoResponseDTO> result = Arrays.asList(
                ToDoResponseDTO.builder()
                        .id(1)
                        .title("마트 다녀오기")
                        .memo("돼지고기 500g 구매")
                        .taskPriority(ToDo.TaskPriority.HIGH)
                        .category("생활")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusDays(1))
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .username("greatcloud13")
                        .build(),

                ToDoResponseDTO.builder()
                        .id(2)
                        .title("프로젝트 완료하기")
                        .memo("테스트 코드 작성 포함")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("업무")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusDays(3))
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .username("greatcloud13")
                        .build(),

                ToDoResponseDTO.builder()
                        .id(3)
                        .title("운동하기")
                        .memo("헬스장 1시간")
                        .taskPriority(ToDo.TaskPriority.MIDDLE)
                        .category("건강")
                        .taskStatus(ToDo.TaskStatus.COMPLETE)
                        .planningDate(LocalDateTime.now().minusDays(1))
                        .doneAt(LocalDateTime.now().minusHours(2))
                        .createdAt(LocalDateTime.now().minusDays(3))
                        .username("greatcloud13")
                        .build()
        );
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

        List<ToDoResponseDTO> urgentTasks = Arrays.asList(
                ToDoResponseDTO.builder()
                        .id(4)
                        .title("고객 미팅 준비")
                        .memo("발표 자료 최종 검토 및 인쇄")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("업무")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusHours(4))
                        .createdAt(LocalDateTime.now().minusHours(1))
                        .username("greatcloud13")
                        .build(),

                ToDoResponseDTO.builder()
                        .id(5)
                        .title("세금 신고서 제출")
                        .memo("마감일 내일까지, 필수 서류 첨부")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("행정")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusDays(1))
                        .createdAt(LocalDateTime.now().minusHours(3))
                        .username("greatcloud13")
                        .build(),

                ToDoResponseDTO.builder()
                        .id(6)
                        .title("의료진 예약 확인")
                        .memo("내과 검진 예약 시간 재확인 필요")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("건강")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusHours(6))
                        .createdAt(LocalDateTime.now().minusHours(2))
                        .username("greatcloud13")
                        .build(),

                ToDoResponseDTO.builder()
                        .id(7)
                        .title("긴급 버그 수정")
                        .memo("프로덕션 환경 critical 이슈 해결")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("업무")
                        .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusHours(2))
                        .createdAt(LocalDateTime.now().minusMinutes(30))
                        .username("greatcloud13")
                        .build()
        );

        return ResponseEntity.ok(urgentTasks);

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

        TodoStats result = TodoStats.builder()
                .total(5)
                .completed(4)
                .completionRate(40)
                .inprogress(1)
                .pending(2)
                .build();

        return ResponseEntity.ok(result);

    }

}
