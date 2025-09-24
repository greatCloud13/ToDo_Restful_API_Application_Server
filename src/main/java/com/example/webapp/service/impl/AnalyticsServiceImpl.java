package com.example.webapp.service.impl;

import com.example.webapp.DTO.SummaryDTO;
import com.example.webapp.DTO.request.SummaryRequestDTO;
import com.example.webapp.common.annotations.InjectUserEntity;
import com.example.webapp.common.context.UserContext;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
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
        result.setCompletionRate(completed*100/completed);
        result.setOverdueCount(todoRepository.countTodosAfterPlanningDate(LocalDateTime.now(), user.getId()));
        result.setUrgentCount(todoRepository.countByUserAndTaskPriority(user, ToDo.TaskPriority.VERY_HIGH));
        result.setActiveDays(todoRepository.countActiveDays(user.getId(), LocalDateTime.now()));

        return result;

    }
}
