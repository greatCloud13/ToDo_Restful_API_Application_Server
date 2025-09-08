package com.example.webapp.DTO;

import com.example.webapp.entity.ToDo;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 할 일 응답 객체
 */
@Data
@Builder
public class ToDoResponseDTO {

    @Schema(description = "고유 id")
    private int id;

    @Schema(description = "제목", example = "마트 다녀오기", maxLength = 50)
    private String title;

    @Schema(description = "메모", example = "돼지고기 500g 구매")
    private String memo;

    @Schema(description = "중요도")
    private ToDo.TaskPriority taskPriority;

    @Schema(description = "상태")
    private ToDo.TaskStatus taskStatus;

    @Schema(description = "카테고리")
    private String category;

    @Schema(description = "생성일자")
    private LocalDateTime createdAt;
}
