package com.example.webapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 인사이트 주의 필요 데이터 DTO
 */
@Data
@AllArgsConstructor
public class WarnInsightDTO {

    /**
     * 할 일 카테고리
     */
    private String category;

    /**
     * 갯수
     */
    private Long count;
    
}
