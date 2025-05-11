package com.todo.api.todo.service;

import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoUpdateRequest;
import com.todo.api.todo.dto.TodoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface TodoService {
  TodoResponse createTodo(Integer memberNo, TodoCreateRequest request);

  Page<TodoResponse> getTodos(Integer memberNo, Pageable pageable);

  TodoResponse getTodo(Integer memberNo, Integer todoNo);

  TodoResponse updateTodo(Integer memberNo, Integer todoNo, TodoUpdateRequest request);

  void deleteTodo(Integer memberNo, Integer todoNo);

  Page<TodoResponse> searchTodos(Integer memberNo, String keyword, Pageable pageable);
}