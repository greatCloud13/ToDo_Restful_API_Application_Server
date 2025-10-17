package com.example.webapp.service;

import com.example.webapp.DTO.QnaDTO;
import com.example.webapp.DTO.request.QnaAnswerRequestDTO;
import com.example.webapp.DTO.request.QnaRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import javax.naming.NoPermissionException;
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

    /**
     * Qna 상세 조회
     * @param id QNA 고유 ID
     * @return QnaDTO 객체
     */
    QnaDTO getQna(Long id);

    /**
     * Qna 답변
     * @param request QNA 답변 DTO
     * @return 답변된 QNA 객체
     */
    QnaDTO answerQna(Long id, QnaAnswerRequestDTO request);

    /**
     * Qna 삭제
     * @param id QNA 고유 ID
     * @return 성공 여부
     */
    boolean deleteQna(Long id);
}
