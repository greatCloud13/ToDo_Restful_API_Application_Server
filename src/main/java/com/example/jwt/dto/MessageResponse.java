package com.example.jwt.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 일반 메시지 응답 DTO
 * 성공/실패 메시지를 클라이언트에 전송할 때 사용
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageResponse {

    /**
     * 응답 메시지
     */
    private String message;

    /**
     * 성공 여부
     */
    @Builder.Default
    private boolean success = true;

    /**
     * 추가 데이터 (선택사항)
     */
    private Object data;

    /**
     * 성공 메시지 생성 (정적 팩토리 메서드)
     *
     * @param message 메시지 내용
     * @return MessageResponse 인스턴스
     */
    public static MessageResponse success(String message) {
        return MessageResponse.builder()
                .message(message)
                .success(true)
                .build();
    }

    /**
     * 성공 메시지와 데이터 함께 생성
     *
     * @param message 메시지 내용
     * @param data 추가 데이터
     * @return MessageResponse 인스턴스
     */
    public static MessageResponse success(String message, Object data) {
        return MessageResponse.builder()
                .message(message)
                .success(true)
                .data(data)
                .build();
    }

    /**
     * 실패 메시지 생성
     *
     * @param message 에러 메시지
     * @return MessageResponse 인스턴스
     */
    public static MessageResponse failure(String message) {
        return MessageResponse.builder()
                .message(message)
                .success(false)
                .build();
    }
}