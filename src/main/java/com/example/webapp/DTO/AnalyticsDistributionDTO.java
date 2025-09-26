package com.example.webapp.DTO;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 분포 분석 API Response DTO
 */
@Data
@RequiredArgsConstructor
public class AnalyticsDistributionDTO {

    private List<CategoryDistribution> categoryDistribution;

    private List<PriorityDistribution> priorityDistribution;

}
