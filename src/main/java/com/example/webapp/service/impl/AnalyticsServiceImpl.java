package com.example.webapp.service.impl;

import com.example.webapp.DTO.AnalyticsTrendDTO;
import com.example.webapp.DTO.SummaryDTO;
import com.example.webapp.DTO.request.AnalyticsTrendRequestDTO;
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
        SummaryRequestDTO.SummaryPeriod period = requestDTO.getPeriod();

//        if(period.equals(SummaryRequestDTO.SummaryPeriod.MONTH)){
//            request.LocalDateTime.now()
//        }

        long total = todoRepository.countByUser(user);
        long completed = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.COMPLETE);
        long pending = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.ON_HOLD);
        long inProgress = todoRepository.countByUserAndStatus(user, ToDo.TaskStatus.IN_PROGRESS);


        result.setTotal(total);
        result.setCompleted(completed);
        result.setPending(pending);
        result.setInProgress(inProgress);
        result.setCompletionRate(completed * 100 / completed);
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

            total = todoRepository.countByUserAndCreatedAtBefore(user, targetDate.atTime(LocalTime.MAX));
            log.info("조회 일자까지의 총 할 일 갯수: {}", total);

            completed = todoRepository.countByUserAndCreatedAtBeforeAndStatus(user, targetDate.atTime(LocalTime.MAX), ToDo.TaskStatus.COMPLETE);
            log.info("조회 일자까지 완료한 할 일 갯수: {}", completed);

            trend.setDate(targetDate);
            trend.setCompleted(completed);
            trend.setTotal(total);
            trend.setCompletionRate(completed*100/total);
            trend.setCreated(todoRepository.countByUserAndCreatedAtBetween(user, startDate, endDate));

            result.add(trend);

        }

        return result;
    }
}
