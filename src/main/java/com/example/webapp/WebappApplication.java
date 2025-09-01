package com.example.webapp;

import com.example.webapp.entity.ToDo;
import com.example.webapp.service.ToDoService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "com.example")
@EnableJpaRepositories(basePackages = "com.example")
@RequiredArgsConstructor
public class WebappApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebappApplication.class, args);
	}

//	DI
	private final ToDoService service;

	public void exe(){
//		전체 검색
		System.out.println("=== 전체 검색 ===");
		for (ToDo row : service.findAllToDo()){
			System.out.println(row);
		}
//		단일 검색
		System.out.println("=== 단일 검색 ===");
		System.out.println(service.findByIdToDo(1));

//		단일 등록
		ToDo todo = new ToDo();
		todo.setTodo("리포지토리 테스트");
		todo.setDetail("db 등록");
		service.insertToDo(todo);
		System.out.println("=== 등록확인 ===");
		System.out.println(service.findByIdToDo(4));

//		업데이트
		ToDo target = service.findByIdToDo(4);
		target.setTodo("리포지토리 테스트 업데이트");
		target.setDetail("DB 업데이트");
//		service.updateToDo(target);
		System.out.println("=== 업데이트 확인 ===");
		System.out.println(service.findByIdToDo(4));

//		삭제
		service.deleteToDo(4);
		System.out.println("=== 삭제 확인 ===");
		for(ToDo row : service.findAllToDo()){
			System.out.println(row);
		}

	}

}
