package com.example.webapp.service;

import com.example.webapp.DTO.QnaDTO;
import com.example.webapp.DTO.request.QnaRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface QnaService {

    /**
     * QNA 생성
     * @param request QNA 요청 DTO
     * @return 생성된 QnaDTO
     */
    QnaDTO saveQna(QnaRequestDTO request);

    /**
     * QNA 리스트 조회
     * @param pageable pageable 요청
     * @return QnaDTO 리스트
     */
    Page<QnaDTO> getQnaList(Pageable pageable);
    
    
}
