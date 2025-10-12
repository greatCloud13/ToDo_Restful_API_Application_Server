package com.example.webapp.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 분포 분석 API Response DTO
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class AnalyticsDistributionDTO {

    private List<CategoryDistribution> categoryDistribution;

    private List<PriorityDistribution> priorityDistribution;

}
