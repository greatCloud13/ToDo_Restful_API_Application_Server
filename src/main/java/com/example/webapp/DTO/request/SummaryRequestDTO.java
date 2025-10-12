package com.example.webapp.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

/**
 *  통계 요약 RequestDTO
 */
@Data
@AllArgsConstructor
public class SummaryRequestDTO {

    @Schema(description = "통계 기간")
    private SummaryPeriod period;
//    private LocalDate startDate;
//
//    private LocalDate endDate;

    @Getter
    public enum SummaryPeriod{
        week("주간"),
        month("월간"),
        year("연간");

        private final String description;

        SummaryPeriod(String period){this.description = period;
        }
    }


}
