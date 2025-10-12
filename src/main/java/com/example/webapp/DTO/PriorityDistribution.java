package com.example.webapp.DTO;

import com.example.webapp.entity.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

/**
 * 분포 분석 API 카테고리 클래스
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriorityDistribution {

    /**
     * 중요도
     */
    private ToDo.TaskPriority priority;


    /**
     * 갯수
     */
    private long count;

}
