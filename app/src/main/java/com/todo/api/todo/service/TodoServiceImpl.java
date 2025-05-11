package com.todo.api.todo.service;

import com.todo.api.mmbr.domain.Member;
import com.todo.api.mmbr.repository.MemberRepository;
import com.todo.api.todo.domain.Todo;
import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoUpdateRequest;
import com.todo.api.todo.dto.TodoResponse;
import com.todo.api.todo.repository.TodoRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TodoServiceImpl implements TodoService {

  private final TodoRepository todoRepository;
  private final MemberRepository memberRepository;

  @Override
  @Transactional
  public TodoResponse createTodo(Integer memberNo, TodoCreateRequest request) {
    Member member = memberRepository.findById(memberNo)
        .orElseThrow(() -> new EntityNotFoundException("Member not found"));

    Todo todo = new Todo();
    todo.setTitle(request.getTitle());
    todo.setDescription(request.getDescription());
    todo.setCompleted(false);
    todo.setMember(member);

    return TodoResponse.from(todoRepository.save(todo));
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TodoResponse> getTodos(Integer memberNo, Pageable pageable) {
    return todoRepository.findByMemberNo(memberNo, pageable)
        .map(TodoResponse::from);
  }

  @Override
  @Transactional(readOnly = true)
  public TodoResponse getTodo(Integer memberNo, Integer todoNo) {
    Todo todo = todoRepository.findById(todoNo)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

    if (!todo.getMember().getNo().equals(memberNo)) {
      throw new IllegalStateException("Not authorized to access this todo");
    }

    return TodoResponse.from(todo);
  }

  @Override
  @Transactional
  public TodoResponse updateTodo(Integer memberNo, Integer todoNo, TodoUpdateRequest request) {
    Todo todo = todoRepository.findById(todoNo)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

    if (!todo.getMember().getNo().equals(memberNo)) {
      throw new IllegalStateException("Not authorized to update this todo");
    }

    todo.setTitle(request.getTitle());
    todo.setDescription(request.getDescription());
    todo.setCompleted(request.isCompleted());

    return TodoResponse.from(todoRepository.save(todo));
  }

  @Override
  @Transactional
  public void deleteTodo(Integer memberNo, Integer todoNo) {
    Todo todo = todoRepository.findById(todoNo)
        .orElseThrow(() -> new EntityNotFoundException("Todo not found"));

    if (!todo.getMember().getNo().equals(memberNo)) {
      throw new IllegalStateException("Not authorized to delete this todo");
    }

    todoRepository.delete(todo);
  }

  @Override
  @Transactional(readOnly = true)
  public Page<TodoResponse> searchTodos(Integer memberNo, String keyword, Pageable pageable) {
    return todoRepository.searchByKeyword(memberNo, keyword, pageable)
        .map(TodoResponse::from);
  }
}