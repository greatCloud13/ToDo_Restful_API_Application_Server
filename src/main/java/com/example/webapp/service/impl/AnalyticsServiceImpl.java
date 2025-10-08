package com.example.webapp.service.impl;

import com.example.webapp.DTO.*;
import com.example.webapp.DTO.request.SummaryRequestDTO;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalyticsServiceImpl implements AnalyticsService {

    private final TodoRepository todoRepository;

    @Override
    @InjectUserEntity
    public SummaryDTO calcSummary(SummaryRequestDTO requestDTO) {

        SummaryDTO result = new SummaryDTO();
        User user = UserContext.getCurrentUser();

        long total = todoRepository.countByUser(user);
        long completed = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.COMPLETE);
        long pending = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.ON_HOLD);
        long inProgress = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.IN_PROGRESS);


        result.setTotal(total);
        result.setCompleted(completed);
        result.setPending(pending);
        result.setInProgress(inProgress);
        if(completed!=0){
            result.setCompletionRate(completed * 100 / completed);
        }else{
            result.setCompletionRate(0);
        }
        result.setOverdueCount(todoRepository.countTodosAfterPlanningDate(LocalDateTime.now(), user.getId()));
        result.setUrgentCount(todoRepository.countByUserAndTaskPriority(user, ToDo.TaskPriority.VERY_HIGH));
        result.setActiveDays(todoRepository.countActiveDays(user.getId(), LocalDateTime.now()));

        return result;

    }

    @InjectUserEntity
    @Transactional(readOnly = true)
    @Override
    public List<AnalyticsTrendDTO> getWeekTrend(String trendPeriod) {

        User user = UserContext.getCurrentUser();

        List<AnalyticsTrendDTO> result = new ArrayList<>();

        LocalDate now = LocalDate.now();

        for(int i=6; i>=0; i--){
            AnalyticsTrendDTO trend = new AnalyticsTrendDTO();
            LocalDate targetDate;
            long total;
            long completed;

            targetDate = now.minusDays(i);
            log.info("조회 일자:{}", targetDate);
            LocalDateTime startDate = targetDate.atTime(LocalTime.MIN);
            LocalDateTime endDate = targetDate.atTime(LocalTime.MAX);

            total = todoRepository.countByUserAndPlanningDateBefore(user, targetDate.atTime(LocalTime.MAX));
            log.info("조회 일자까지의 총 할 일 갯수: {}", total);

            completed = todoRepository.countByUserAndPlanningDateBeforeAndStatus(user, targetDate.atTime(LocalTime.MAX), ToDo.TaskStatus.COMPLETE);
            log.info("조회 일자까지 완료한 할 일 갯수: {}", completed);

            trend.setDate(targetDate);
            trend.setCompleted(completed);
            trend.setTotal(total);
            if(completed!=0) {
                trend.setCompletionRate(completed * 100 / total);
            }else{
                trend.setCompletionRate(0);
            }

            trend.setCreated(todoRepository.countByUserAndCreatedAtBetween(user, startDate, endDate));

            result.add(trend);

        }

        return result;
    }

    @InjectUserEntity
    @Transactional(readOnly = true)
    @Override
    public AnalyticsDistributionDTO getDistribution(String period) {

        User user = UserContext.getCurrentUser();

        LocalDate now = LocalDate.now();
        LocalDateTime startDate = now.minusDays(6).atTime(LocalTime.MIN);

        List<PriorityDistribution> priorityResult = todoRepository.countByUserAndTaskPriorityDistribution(user.getId(), startDate);
        List<CategoryDistribution> categoryResult = todoRepository.countByUserAndCategoryAndTask(user.getId(), startDate);


        return new AnalyticsDistributionDTO(categoryResult, priorityResult);
    }

    @InjectUserEntity
    @Override
    public List<InsightDTO> getInsight() {

        List<InsightDTO> result = new ArrayList<>();
        User user = UserContext.getCurrentUser();

        BestProducibilityDateDTO bestProducibilityDateResult = todoRepository.getBestProducibilityDate(user);
        InsightDTO bestProducibility = new InsightDTO(
                "positive",
                "최고 작업일",
                bestProducibilityDateResult.getDate()+"에 가장 많은 작업을 완료했습니다. 완료율: "+bestProducibilityDateResult.getProgress()+"%"
        );

        List<WarnInsightDTO> warnInsight = todoRepository.getWarnInsight(user);

        for(WarnInsightDTO insight : warnInsight){
            result.add( new InsightDTO(
                    "warn",
                    "주의 필요",
                    insight.getCategory()+"카테고리에서 "+insight.getCount()+"개의 지연된 할 일이 있습니다."));
        }

        result.add(bestProducibility);

        return result;
    }
}
