package com.example.webapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 인사이트 Response DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class InsightDTO {

    /**
     * 해당 메시지의 구분
     */
    private String level;

    /**
     * 제목
     */
    private String title;

    /**
     * 메시지
     */
    private String message;

}
