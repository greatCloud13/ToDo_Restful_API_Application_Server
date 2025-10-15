package com.example.webapp.DTO;

import com.example.webapp.entity.Qna;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
public class QnaDTO {

    /**
     * 문의 ID
     */
    private Long id;

    /**
     * 문의 제목
     */
    private String title;

    /**
     * 문의 내용
     */
    private String content;

    /**
     * 답변 내용
     */
    private String answer;

    /**
     * 문의자명
     */
    private String owner;

    /**
     * 답변자
     */
    private String respondent;

    /**
     * 등록 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    /**
     * 답변 여부
     */
    private Boolean isAnswered;

    /**
     * 답변 일자
     */
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime answeredAt;

    public static QnaDTO from (Qna qna){
        return QnaDTO.builder()
                .id(qna.getId())
                .title(qna.getTitle())
                .content(qna.getContent())
                .answer(qna.getAnswer())
                .owner(qna.getOwner().getUsername())
                .respondent("admin")
                .isAnswered(qna.getIsAnswered())
                .build();

    }

}
