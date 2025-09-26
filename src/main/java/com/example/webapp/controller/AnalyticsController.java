package com.example.webapp.controller;

import com.example.webapp.DTO.AnalyticsDistributionDTO;
import com.example.webapp.DTO.AnalyticsTrendDTO;
import com.example.webapp.DTO.SummaryDTO;
import com.example.webapp.DTO.request.AnalyticsTrendRequestDTO;
import com.example.webapp.DTO.request.SummaryRequestDTO;
import com.example.webapp.service.AnalyticsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @Operation(
            summary = "통계 요약 요청 API",
            description = """
                    ## 통계 요약 요청 API
                    선택한 기간의 통계 요약을 조회합니다.
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/summary")
    public ResponseEntity<SummaryDTO> summary(SummaryRequestDTO request){

        SummaryDTO result = analyticsService.calcSummary(request);

        return ResponseEntity.ok(result);

    }

    @Operation(
            summary = "일별 완료 추이 요청 API",
            description = """
                    ## 일별 완료 추이 요청 API
                    일주일 간의 완료 추이를 요청합니다.
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/trend")
    public ResponseEntity<List<AnalyticsTrendDTO>> trend(@RequestParam("period") String trendPeriod){

        List<AnalyticsTrendDTO> result = analyticsService.getWeekTrend(trendPeriod);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = " 분포 분석 요청 API",
            description = """
                    ## 분포 분석 요청 API
                    분포분석 그래프에 필요한 데이터를 요청합니다.
                    - 개발일자:
                    - 수정일자: .
                    - 테스트 여부:

                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/distribution")
    public ResponseEntity<AnalyticsDistributionDTO> distribution(@RequestParam("period") String trendPeriod){

        AnalyticsDistributionDTO result = new AnalyticsDistributionDTO();

        return ResponseEntity.ok(result);
    }



}
