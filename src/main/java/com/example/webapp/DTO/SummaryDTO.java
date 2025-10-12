package com.example.webapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class SummaryDTO {

    @Schema(description = "전체 할 일 갯수")
    private long total;

    @Schema(description = "완료한 할 일 갯수")
    private long completed;

    @Schema(description = "지연된 할 일 갯수")
    private long pending;

    @Schema(description = "진행중인 할 일 갯수")
    private long inProgress;

    @Schema(description = "완료율")
    private long completionRate;

    @Schema(description = "마감 기한이 지난 할 일 갯수")
    private long overdueCount;

    @Schema(description = "긴급한 할 일 갯수")
    private long urgentCount;

    @Schema(description = "일간 평균 완료율")
    private double avgDailyCompletion;

    @Schema(description = "생산성 변화율")
    private double trend;

    @Schema(description = "활동한 일자")
    private long activeDays;


}
