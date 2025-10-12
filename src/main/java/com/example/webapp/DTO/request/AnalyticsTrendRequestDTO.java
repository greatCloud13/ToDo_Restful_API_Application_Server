package com.example.webapp.DTO.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

/**
 * 완료 추이 RequestDTO
 */
@Data
@AllArgsConstructor
public class AnalyticsTrendRequestDTO {

    @Schema(description = "통계 기간")
    private SummaryPeriod TrendPeriod;

    @Getter
    private enum SummaryPeriod{
        WEEK("주간"),
        MONTH("월간"),
        YEAR("연간");

        private final String description;

        SummaryPeriod(String period){this.description = period;
        }
    }


}
