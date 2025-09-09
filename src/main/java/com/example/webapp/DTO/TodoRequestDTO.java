package com.example.webapp.DTO;

import com.example.webapp.entity.ToDo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 할 일 등록 DTO
 */
@Data
@Builder
public class TodoRequestDTO {

    @Schema(description = "제목")
    private String title;
    
    @Schema(description = "메모")
    private String memo;

    @Schema(description = "우선순위")
    private ToDo.TaskPriority priority;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "상태")
    private ToDo.TaskStatus status;

    @Schema(description = "마감일")
    private LocalDateTime targetDate;

}