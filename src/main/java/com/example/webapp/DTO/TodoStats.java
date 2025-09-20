package com.example.webapp.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 대시보드 통계 응답 DTO
 */
@Data
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
public class TodoStats {

    @Schema(description = "전체 할 일")
    private long total;

    @Schema(description = "완료된 할 일")
    private long completed;

    @Schema(description = "진행중인 할 일")
    private long inprogress;

    @Schema(description = "보류 중")
    private long pending;

    @Schema(description = "진행률")
    private long completionRate;
}
