package com.example.webapp.DTO;

import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

/**
 * 할 일 응답 객체
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
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

    @Schema(description = "마감일")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime planningDate;

    @Schema(description = "종료일")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime doneAt;

    @Schema(description = "생성일자")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    @Schema(description = "사용자")
    private String username;


    public static ToDoResponseDTO from(ToDo todo) {
        return ToDoResponseDTO.builder()
                .id(todo.getId())
                .title(todo.getTitle())
                .memo(todo.getMemo())
                .taskStatus(todo.getStatus())
                .taskPriority(todo.getTaskPriority())
                .planningDate(todo.getPlanningDate())
                .doneAt(todo.getDoneAt())
                .category(todo.getCategory())
                .createdAt(todo.getCreatedAt())
                .username(todo.getUser().getUsername())
                .build();
    }

}
