package com.example.webapp.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.scheduling.config.Task;

import java.time.LocalDateTime;

/**
 * ToDoNote Entity
 */
@Entity
@Table(name = "todos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ToDo {
    /**
     * ToDoContext ID
     *
     */
    @Id
    @GeneratedValue
    @Schema(description = "ID", example = "1")
    private Integer id;

    /**
     * ToDoContext Title
     */
    @Column(name="title", nullable = false, length = 50)
    @Schema(description = "제목", example = "마트 다녀오기", maxLength = 50)
    private String title;

    /**
     * 우선순위
     */
    @Column(name="priority", nullable = false)
    @Builder.Default
    private TaskPriority taskPriority = TaskPriority.MIDDLE;

    /**
     * 카테고리
     * 기본 = 일상
     */
    @Column(name="category")
    @Builder.Default
    private String category = "일상";

    /**
     * ToDoContext Description
     *
     */
    @Column(name="memo", nullable = true, length = 200)
    @Schema(description = "메모", example = "돼지고기 500g 구매")
    private String memo;

    /**
     * 할 일 상태
     */
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    @Builder.Default
    private TaskStatus status = TaskStatus.IN_PROGRESS;

    /**
     * 예정 종료일자
     */
    @Column(name="planning_date", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime planningDate;

    /**
     * 완료 시각
     */
    @Column(name="done_at", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime doneAt;

    /**
     * 작성 일자
     */
    @CreationTimestamp
    @Column(name ="created_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime createdAt;

    /**
     * 업데이트 일자
     */
    @UpdateTimestamp
    @Column(name ="updated_at", nullable = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    private LocalDateTime updatedAt;

    /**
     * 소유자
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false,
    foreignKey = @ForeignKey(name = "fk_todo_user"))
    private User user;

    /**
     * 할 일 상태 Enum
     */
    @Getter
    public enum TaskStatus{
        IN_PROGRESS("진행중"),
        COMPLETE("완료"),
        ON_HOLD("보류");

        private final String description;

        TaskStatus(String description){
            this.description = description;
        }
    }

    /**
     * 우선순위 ENUM
     */
    @Getter
    public enum TaskPriority{
        VERY_HIGH("매우 높음"),
        HIGH("높음"),
        MIDDLE("중간"),
        LOW("낮음"),
        VERY_LOW("매우 낮음");

        private String description;

        TaskPriority(String description){
            this.description = description;
        }
    }
}
