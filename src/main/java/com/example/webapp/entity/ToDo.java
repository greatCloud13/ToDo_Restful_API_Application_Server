package com.example.webapp.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

//할일: 엔티티
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ToDo {
//    할일 ID
    private Integer id;
//    할일
    private String todo;
//    할일 상세보기
    private String detail;
//    작성 일자
    private LocalDateTime createdAt;
//    업데이트 일자
    private LocalDateTime updatedAt;
}
