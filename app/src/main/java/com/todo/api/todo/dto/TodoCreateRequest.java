package com.todo.api.todo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoCreateRequest {
  private String title;
  private String description;
}