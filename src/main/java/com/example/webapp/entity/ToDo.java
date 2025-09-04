package com.example.webapp.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//할일: 엔티티
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {


    /**
     * ToDoContext ID
     *
     */
    @Schema(description = "ToDo 게시물 ID", example = "1")
    private Integer id;

    /**
     * ToDoContext Title
     */
    @Schema(description = "ToDo 제목", example = "마트 다녀오기", maxLength = 50)
    private String todo;

    /**
     * ToDoContext Description
     *
     */
    @Schema(description = "ToDo 상세 내용", example = "돼지고기 500g 구매")
    private String detail;

    /**
     * 작성 일자
     */
    private LocalDateTime createdAt;

    /**
     * 업데이트 일자
     */
    private LocalDateTime updatedAt;

}
