package com.todo.api.todo.dto;

import com.todo.api.todo.domain.Todo;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoResponse {
  private Integer no;
  private String title;
  private String description;
  private boolean completed;
  private Integer memberNo;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;

  public static TodoResponse from(Todo todo) {
    TodoResponse response = new TodoResponse();
    response.setNo(todo.getNo());
    response.setTitle(todo.getTitle());
    response.setDescription(todo.getDescription());
    response.setCompleted(todo.isCompleted());
    response.setMemberNo(todo.getMember().getNo());
    response.setCreatedAt(todo.getCreatedAt());
    response.setUpdatedAt(todo.getUpdatedAt());
    return response;
  }
}