package com.example.webapp.DTO.request;
import com.example.webapp.entity.ToDo;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 대량 수정 요청 DTO
 */
@Data
@NoArgsConstructor
public class BulkUpdateRequest {

    /**
     * 수정 대상 id 리스트
     */
    private List<Long> id;

    /**
     * 수정할 상태
     */
    private ToDo.TaskStatus status;

}
