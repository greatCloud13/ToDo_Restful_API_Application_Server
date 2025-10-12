package com.example.webapp.DTO;


import com.example.webapp.entity.ToDo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * 분포 분석 API 카테고리 클래스
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class CategoryDistribution {

    /**
     * 카테고리 이름
     */
    private String categoryName;

    /**
     * 완료된 할 일 갯수
     */
    private long completed;

    /**
     * 진행중인 할 일 갯수
     */
    private long inProgress;

    /**
     * 보류중인 할 일 갯수
     */
    private long pending;

    /**
     * 현재 카테고리 완료율
     */
    private long completionRate;

}
