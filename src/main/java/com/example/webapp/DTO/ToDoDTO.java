package com.example.webapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class ToDoDTO {
    //    할일
    @Schema(description = "ToDo 제목", example = "마트 다녀오기", maxLength = 50)
    private String todo;
    //    할일 상세보기
    @Schema(description = "ToDo 상세 내용", example = "돼지고기 500g 구매")
    private String detail;
}
