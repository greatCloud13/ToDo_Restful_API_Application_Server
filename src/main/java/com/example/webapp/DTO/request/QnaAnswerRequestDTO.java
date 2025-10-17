package com.example.webapp.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QnaAnswerRequestDTO {
    /**
     * 답변 내용
     */
    private String answer;

}
