package com.example.webapp.controller;

import com.example.webapp.DTO.QnaDTO;
import com.example.webapp.DTO.request.QnaAnswerRequestDTO;
import com.example.webapp.DTO.request.QnaRequestDTO;
import com.example.webapp.entity.Qna;
import com.example.webapp.service.QnaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Delete;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/support")
@Tag(name = "QNA", description = "QNA 관리 API")
public class SupportController {

    private final QnaService qnaService;

    @Operation(
            summary = "QNA 등록",
            description = """
                    ## QNA 등록 API
                    요청한 사용자의 QNA를 등록합니다.
                    - 개발일자: 2025-10-15
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    Pageable  pageable
                    """
    )
    @PostMapping
    public ResponseEntity<QnaDTO> createQna(@RequestBody QnaRequestDTO request){

        log.debug("요청 내용 {}",request);

        QnaDTO result = qnaService.saveQna(request);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "QNA 리스트 조회",
            description = """
                    ## QNA 리스트 조회 API
                    등록한 QNA 리스트를 조회합니다.
                    - 개발일자: 2025-10-15
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    Pageable  pageable
                    """
    )
    @GetMapping(value = {"", "/"})
    public ResponseEntity<Page<QnaDTO>> getQnaList(Pageable pageable){

        Page<QnaDTO> result = qnaService.getQnaList(pageable);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "QNA 상세 조회",
            description = """
                    ## QNA 상세 조회 API
                    등록되어 있는 QNA의 상세정보를 조회합니다.
                    - 개발일자: 2025-10-16
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    """
    )
    @GetMapping("/{id}")
    public ResponseEntity<QnaDTO> getQna(@PathVariable Long id){

        QnaDTO result = qnaService.getQna(id);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "QNA 답변",
            description = """
                    ## QNA 답변 API
                    등록되어 있는 QNA 답변을 등록합니다.
                    - 개발일자: 2025-10-17
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    """
    )
    @PatchMapping("/{id}")
    public ResponseEntity<QnaDTO> answerQna(@PathVariable Long id, @RequestBody QnaAnswerRequestDTO request){

        QnaDTO result = qnaService.answerQna(id, request);

        return ResponseEntity.ok(result);
    }

    @Operation(
            summary = "QNA 삭제",
            description = """
                    ## QNA 삭제 API
                    등록되어 있는 QNA를 삭제합니다.
                    - 개발일자: 2025-10-17
                    - 수정일자:
                    
                    ### 필수 입력 항목
                    """
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Boolean> deleteQna(@PathVariable Long id){

        Boolean result = qnaService.deleteQna(id);

        return ResponseEntity.ok(result);
    }






























}
