package com.example.webapp.service;

import com.example.jwt.Repository.UserRepository;
import com.example.webapp.DTO.ToDoResponseDTO;
import com.example.webapp.entity.Role;
import com.example.webapp.entity.ToDo;
import com.example.webapp.entity.User;
import com.example.webapp.fixture.ToDoTestFixture;
import com.example.webapp.repository.TodoRepository;
import com.example.webapp.service.impl.ToDoServiceImpl;
import io.jsonwebtoken.lang.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ToDoServiceTest {

    private User testuser;
    private Pageable testPageable;
    private List<ToDo> testToDoList;
    private Page<ToDo> testToDoPage;

    @Mock
    private TodoRepository todoRepository;

    @InjectMocks
    private ToDoServiceImpl toDoService;

    @BeforeEach
    void setUp() {

//        테스트 사용자 객체 생성
        testuser = ToDoTestFixture.createTestuser();
        System.out.println("테스트 사용자 객체 생성 완료");
//        테스트 Pageable 생성
        testPageable = ToDoTestFixture.createTestPageable();
        System.out.println("테스트 Pageable 객체 생성 완료");
//        테스트 할 일 리스트 생성
        testToDoList = ToDoTestFixture.createTestTodoList(testuser);
        System.out.println("테스트 할 일 리스트 생성 완료.");
//        테스트 할 일 페이지 생성
        testToDoPage = new PageImpl<>(
                testToDoList,
                testPageable,
                3
        );
        System.out.println("테스트 할 일 페이지 생성 완료.");

        System.out.println("==============================================");
        System.out.println("테스트 데이터 준비 완료.");

    }

    @Test
    void 시작_테스트(){
        assertTrue(true);
        System.out.println("테스트가 실행되었습니다");
    }

    @Test
    void 올바른_id와_사용자명을_사용하여_리스트를_호출한_경우(){
        // Arrange (준비)
        System.out.println("=========올바른 id및 사용자명을 사용한 리스트 호출 테스트==============");
        when(todoRepository.findByUser(testuser, testPageable)).thenReturn(testToDoPage);

        // ACT (실행)
        Page<ToDoResponseDTO> result = toDoService.findTodoListByUser(testuser, testPageable);

        // Assert (검증)
        assertNotNull(result);          //null 확인
        assertTrue(result.hasContent());    //내용이 있는지 확인
        assertEquals(testToDoPage.getContent().size(), result.getContent().size()); //페이지 크기 확인
        assertEquals(testToDoPage.getTotalElements(), result.getTotalElements()); //전체 요소 개수 확인
        assertEquals(testToDoPage.getContent().getFirst().getTitle(), result.getContent().getFirst().getTitle()); //첫번째 항목 제목 확인

        // Mock 메서드 호출 검증
        verify(todoRepository, times(1)).findByUser(testuser, testPageable);
    }


}
