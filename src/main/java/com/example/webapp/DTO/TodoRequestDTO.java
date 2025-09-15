package com.example.webapp.DTO;

import com.example.webapp.entity.ToDo;
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
 * 할 일 등록 DTO
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
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
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime targetDate;

}