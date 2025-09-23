package com.example.webapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class SummaryDTO {

    @Schema()
    private long total;

    private long completed;

    private long pending;

    private long inProgress;

    private long completionRate;

    private long overdueCount;

    private long urgentCount;

    private double avgDailyCompletion;

    private double trend;

    private long activeDays;


}
