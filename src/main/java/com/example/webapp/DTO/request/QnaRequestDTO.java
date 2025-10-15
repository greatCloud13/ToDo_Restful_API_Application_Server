package com.example.webapp.DTO.request;

import com.example.webapp.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnaRequestDTO {

    /**
     * 문의 제목
     */
    private String title;

    /**
     * 문의 내용
     */
    private String content;

}
