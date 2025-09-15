package com.example.webapp.fixture;

import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class ToDoTestFixture {

    //    테스트용 User 객체 생성
    public static User createTestuser() {
        return User.builder()
                .id(1L)
                .username("username")
                .email("testUser@test.com")
                .password("encodedPassword")
                .createdAt(LocalDateTime.now().minusDays(1))
                .build();
    }

    //    테스트용 할 일 단일 객체 생성
    public static ToDo createToDo(User testUser){
        return ToDo.builder()
                .id(1)
                .title("마트 다녀오기")
                .memo("돼지고기 500g, 양파 3개 구매")
                .taskPriority(ToDo.TaskPriority.HIGH)
                .category("생활")
                .status(ToDo.TaskStatus.IN_PROGRESS)
                .planningDate(LocalDateTime.of(2025, 9, 16, 14, 30))
                .createdAt(LocalDateTime.now().minusDays(1))
                .user(testUser)
                .build();
    }

    //    테스트용 할 일 리스트
    public static List<ToDo> createTestTodoList(User testUser) {
        return Arrays.asList(
                ToDo.builder()
                        .id(1)
                        .title("마트 다녀오기")
                        .memo("돼지고기 500g 구매")
                        .taskPriority(ToDo.TaskPriority.HIGH)
                        .category("생활")
                        .status(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusDays(1))
                        .createdAt(LocalDateTime.now().minusDays(1))
                        .user(testUser)
                        .build(),

                ToDo.builder()
                        .id(2)
                        .title("프로젝트 완료하기")
                        .memo("테스트 코드 작성 포함")
                        .taskPriority(ToDo.TaskPriority.VERY_HIGH)
                        .category("업무")
                        .status(ToDo.TaskStatus.IN_PROGRESS)
                        .planningDate(LocalDateTime.now().plusDays(3))
                        .createdAt(LocalDateTime.now().minusDays(2))
                        .user(testUser)
                        .build(),

                ToDo.builder()
                        .id(3)
                        .title("운동하기")
                        .memo("헬스장 1시간")
                        .taskPriority(ToDo.TaskPriority.MIDDLE)
                        .category("건강")
                        .status(ToDo.TaskStatus.COMPLETE)
                        .planningDate(LocalDateTime.now().minusDays(1))
                        .doneAt(LocalDateTime.now().minusHours(2))
                        .createdAt(LocalDateTime.now().minusDays(3))
                        .user(testUser)
                        .build()
        );
    }

    //    테스트용 할 일 단일 DTO 객체 생성
    public static ToDoResponseDTO createToDoResponseDTO(){
        return ToDoResponseDTO.builder()
                .id(1)
                .title("마트 다녀오기")
                .memo("돼지고기 500g, 양파 3개 구매")
                .taskPriority(ToDo.TaskPriority.HIGH)
                .taskStatus(ToDo.TaskStatus.IN_PROGRESS)
                .category("생활")
                .planningDate(LocalDateTime.of(2025, 9, 16, 14, 30))
                .createdAt(LocalDateTime.now().minusDays(1))
                .username("testUser")
                .build();
    }

    //    테스트용 pageable 객체 생성
    public static Pageable createTestPageable(){
        return PageRequest.of(0, 10);
    }


}
