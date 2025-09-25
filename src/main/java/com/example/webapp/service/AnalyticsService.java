package com.example.webapp.service;

import com.example.webapp.DTO.AnalyticsTrendDTO;
import com.example.webapp.DTO.SummaryDTO;
import com.example.webapp.DTO.request.AnalyticsTrendRequestDTO;
import com.example.webapp.DTO.request.SummaryRequestDTO;

import java.util.List;

public interface AnalyticsService {

    SummaryDTO calcSummary(SummaryRequestDTO requestDTO);

    List<AnalyticsTrendDTO> getWeekTrend(String trendPeriod);
    }
