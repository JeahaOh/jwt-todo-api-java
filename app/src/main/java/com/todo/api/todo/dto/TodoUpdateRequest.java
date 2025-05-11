package com.todo.api.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoUpdateRequest {
  private String title;
  private String description;
  private boolean completed;
}