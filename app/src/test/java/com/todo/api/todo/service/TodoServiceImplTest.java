package com.todo.api.todo.service;

import com.todo.api.mmbr.domain.Member;
import com.todo.api.mmbr.repository.MemberRepository;
import com.todo.api.todo.domain.Todo;
import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoResponse;
import com.todo.api.todo.dto.TodoUpdateRequest;
import com.todo.api.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
@Tag("unit")
@Transactional
class TodoServiceImplTest {

  @Autowired
  private TodoService todoService;

  @Autowired
  private TodoRepository todoRepository;

  @Autowired
  private MemberRepository memberRepository;

  private Member testMember;
  private Todo testTodo;

  @BeforeEach
  void setUp() {
    // 테스트 회원 생성
    testMember = new Member();
    testMember.setName("테스트 사용자");
    testMember.setEmail("test@example.com");
    testMember.setPassword("password123");
    memberRepository.save(testMember);

    // 테스트 Todo 생성
    testTodo = new Todo();
    testTodo.setTitle("첫 번째 할 일");
    testTodo.setDescription("테스트 설명");
    testTodo.setCompleted(false);
    testTodo.setMember(testMember);
    todoRepository.save(testTodo);
  }

  @Test
  @DisplayName("Todo 생성이 정상적으로 동작하는지 확인")
  void createTodo() {
    // given
    TodoCreateRequest request = new TodoCreateRequest();
    request.setTitle("테스트 제목");
    request.setDescription("테스트 설명");

    // when
    TodoResponse response = todoService.createTodo(testMember.getNo(), request);

    // then
    assertThat(response.getTitle()).isEqualTo(request.getTitle());
    assertThat(response.getDescription()).isEqualTo(request.getDescription());
    assertThat(response.isCompleted()).isFalse();

    Todo savedTodo = todoRepository.findById(response.getNo()).orElseThrow();
    assertThat(savedTodo.getTitle()).isEqualTo(request.getTitle());
    assertThat(savedTodo.getDescription()).isEqualTo(request.getDescription());
    assertThat(savedTodo.getMember().getNo()).isEqualTo(testMember.getNo());
  }

  @Test
  @DisplayName("존재하지 않는 회원으로 Todo 생성 시 예외 발생")
  void createTodoWithNonExistentMember() {
    // given
    Integer memberNo = 999;
    TodoCreateRequest request = new TodoCreateRequest();

    // when & then
    assertThatThrownBy(() -> todoService.createTodo(memberNo, request))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Member not found");
  }

  @Test
  @DisplayName("Todo 목록 조회가 정상적으로 동작하는지 확인")
  void getTodos() {
    // given
    Pageable pageable = PageRequest.of(0, 10);

    // when
    Page<TodoResponse> response = todoService.getTodos(testMember.getNo(), pageable);

    // then
    assertThat(response.getContent()).isNotEmpty();
    assertThat(response.getContent().get(0).getTitle()).isEqualTo(testTodo.getTitle());
  }

  @Test
  @DisplayName("Todo 수정이 정상적으로 동작하는지 확인")
  void updateTodo() {
    // given
    TodoUpdateRequest request = new TodoUpdateRequest();
    request.setTitle("수정된 제목");
    request.setDescription("수정된 설명");
    request.setCompleted(true);

    // when
    TodoResponse response = todoService.updateTodo(testMember.getNo(), testTodo.getNo(), request);

    // then
    assertThat(response.getTitle()).isEqualTo(request.getTitle());
    assertThat(response.getDescription()).isEqualTo(request.getDescription());
    assertThat(response.isCompleted()).isEqualTo(request.isCompleted());

    Todo updatedTodo = todoRepository.findById(testTodo.getNo()).orElseThrow();
    assertThat(updatedTodo.getTitle()).isEqualTo(request.getTitle());
    assertThat(updatedTodo.getDescription()).isEqualTo(request.getDescription());
    assertThat(updatedTodo.isCompleted()).isEqualTo(request.isCompleted());
  }

  @Test
  @DisplayName("다른 회원의 Todo 수정 시도 시 예외 발생")
  void updateTodoWithWrongMember() {
    // given
    Member otherMember = new Member();
    otherMember.setName("다른 사용자");
    otherMember.setEmail("other@example.com");
    otherMember.setPassword("password123");
    memberRepository.save(otherMember);

    TodoUpdateRequest request = new TodoUpdateRequest();

    // when & then
    assertThatThrownBy(() -> todoService.updateTodo(otherMember.getNo(), testTodo.getNo(), request))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Not authorized to update this todo");
  }

  @Test
  @DisplayName("Todo 삭제가 정상적으로 동작하는지 확인")
  void deleteTodo() {
    // given
    Integer todoNo = testTodo.getNo();

    // when
    todoService.deleteTodo(testMember.getNo(), todoNo);

    // then
    assertThat(todoRepository.findById(todoNo)).isEmpty();
  }

  @Test
  @DisplayName("Todo 검색이 정상적으로 동작하는지 확인")
  void searchTodos() {
    // given
    String keyword = "할 일";
    Pageable pageable = PageRequest.of(0, 10);

    // when
    Page<TodoResponse> response = todoService.searchTodos(testMember.getNo(), keyword, pageable);

    // then
    assertThat(response.getContent()).isNotEmpty();
    assertThat(response.getContent().get(0).getTitle()).contains(keyword);
  }

  @Test
  @DisplayName("Todo 조회가 정상적으로 동작하는지 확인")
  void getTodo() {
    // given
    Integer todoNo = testTodo.getNo();

    // when
    TodoResponse response = todoService.getTodo(testMember.getNo(), todoNo);

    // then
    assertThat(response.getTitle()).isEqualTo(testTodo.getTitle());
    assertThat(response.getDescription()).isEqualTo(testTodo.getDescription());
    assertThat(response.isCompleted()).isEqualTo(testTodo.isCompleted());
  }

  @Test
  @DisplayName("존재하지 않는 Todo 조회 시 예외 발생")
  void getTodoWithNonExistentTodo() {
    // given
    Integer todoNo = 999;

    // when & then
    assertThatThrownBy(() -> todoService.getTodo(testMember.getNo(), todoNo))
        .isInstanceOf(EntityNotFoundException.class)
        .hasMessage("Todo not found");
  }

  @Test
  @DisplayName("다른 회원의 Todo 조회 시도 시 예외 발생")
  void getTodoWithWrongMember() {
    // given
    Member otherMember = new Member();
    otherMember.setName("다른 사용자");
    otherMember.setEmail("other@example.com");
    otherMember.setPassword("password123");
    memberRepository.save(otherMember);

    // when & then
    assertThatThrownBy(() -> todoService.getTodo(otherMember.getNo(), testTodo.getNo()))
        .isInstanceOf(IllegalStateException.class)
        .hasMessage("Not authorized to access this todo");
  }
}