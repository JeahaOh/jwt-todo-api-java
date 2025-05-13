package com.todo.api.common.security;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/todos")
public class TestController {

  @GetMapping
  public ResponseEntity<String> getAllTodos() {
    return ResponseEntity.ok("모든 할일 목록");
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<String> getTodoById(@PathVariable Long id) {
    return ResponseEntity.ok("할일 상세 정보: " + id);
  }
}