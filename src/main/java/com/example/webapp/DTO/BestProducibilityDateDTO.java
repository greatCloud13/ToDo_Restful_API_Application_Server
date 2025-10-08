package com.example.webapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 인사이트 최고 생산성 DTO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BestProducibilityDateDTO {

    private LocalDate date;

    private Long progress;

    public BestProducibilityDateDTO(LocalDateTime dateTime, Long progress) {
        this.date = dateTime.toLocalDate();
        this.progress = progress;
    }
}
