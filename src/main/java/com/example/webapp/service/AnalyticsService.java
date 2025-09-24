package com.example.webapp.service;

import com.example.webapp.DTO.SummaryDTO;
import com.example.webapp.DTO.request.SummaryRequestDTO;

public interface AnalyticsService {

    SummaryDTO calcSummary(SummaryRequestDTO requestDTO);
}
