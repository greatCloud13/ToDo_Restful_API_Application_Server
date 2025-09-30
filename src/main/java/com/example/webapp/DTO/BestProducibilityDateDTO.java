package com.example.webapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

/**
 * 인사이트 최고 생산성 DTO
 */
@Data
@AllArgsConstructor
public class BestProducibilityDateDTO {

    private LocalDate date;

    private Long progress;

}
