package com.example.webapp.DTO.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SummaryRequestDTO {

    private SummaryPeriod period;

    private LocalDate startDate;

    private LocalDate endDate;

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
