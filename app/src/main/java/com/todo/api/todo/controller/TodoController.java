package com.todo.api.todo.controller;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.util.JwtUtil;
import com.todo.api.common.util.ResponseUtil;
import com.todo.api.todo.dto.TodoResponse;
import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoUpdateRequest;
import com.todo.api.todo.service.TodoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/todos")
@RequiredArgsConstructor
@Tag(name = "02. Todo", description = "Todo API")
public class TodoController {

  private final TodoService todoService;
  private final JwtUtil jwtUtil;

  @Operation(summary = "Todo 등록", description = "새로운 Todo를 생성합니다. completed는 항상 false로 설정됩니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 생성 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
      @ApiResponse(responseCode = "404", description = "회원을 찾을 수 없음")
  })
  @PostMapping
  public ResponseEntity<CustomResponse<TodoResponse>> createTodo(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "Todo 생성 정보", required = true) @RequestBody TodoCreateRequest request) {
    return ResponseEntity.ok(ResponseUtil.success(todoService.createTodo(getMemberNo(authentication), request)));
  }

  @Operation(summary = "모든 Todos 조회", description = "현재 로그인한 회원의 모든 Todo 목록을 페이징하여 조회합니다. 정렬 기준: createdAt(생성일), updatedAt(수정일), no, title")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 목록 조회 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
  })
  @GetMapping
  public ResponseEntity<CustomResponse<Page<TodoResponse>>> getTodos(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "정렬 기준 (createdAt, updatedAt, no, title)", required = false) @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
      @Parameter(description = "정렬 방향 (asc, desc)", required = false) @RequestParam(required = false, defaultValue = "desc") String direction,
      @PageableDefault(size = 10) Pageable pageable) {
    Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return ResponseEntity.ok(ResponseUtil.success(todoService.getTodos(getMemberNo(authentication), sortedPageable)));
  }

  @Operation(summary = "특정 Todo 조회", description = "특정 ID의 Todo를 조회합니다. 자신의 Todo만 조회 가능합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 조회 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
      @ApiResponse(responseCode = "403", description = "접근 권한 없음"),
      @ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
  })
  @GetMapping("/{id}")
  public ResponseEntity<CustomResponse<TodoResponse>> getTodo(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "Todo ID", required = true) @PathVariable("id") Integer todoNo) {
    return ResponseEntity.ok(ResponseUtil.success(todoService.getTodo(getMemberNo(authentication), todoNo)));
  }

  @Operation(summary = "Todo 수정", description = "특정 ID의 Todo를 수정합니다. 자신의 Todo만 수정 가능합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 수정 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
      @ApiResponse(responseCode = "403", description = "수정 권한 없음"),
      @ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
  })
  @PutMapping("/{id}")
  public ResponseEntity<CustomResponse<TodoResponse>> updateTodo(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "Todo ID", required = true) @PathVariable("id") Integer todoNo,
      @Parameter(description = "Todo 수정 정보", required = true) @RequestBody TodoUpdateRequest request) {
    return ResponseEntity
        .ok(ResponseUtil.success(todoService.updateTodo(getMemberNo(authentication), todoNo, request)));
  }

  @Operation(summary = "Todo 삭제", description = "특정 ID의 Todo를 삭제합니다. 자신의 Todo만 삭제 가능합니다.")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 삭제 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청"),
      @ApiResponse(responseCode = "403", description = "삭제 권한 없음"),
      @ApiResponse(responseCode = "404", description = "Todo를 찾을 수 없음")
  })
  @DeleteMapping("/{id}")
  public ResponseEntity<CustomResponse<Void>> deleteTodo(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "Todo ID", required = true) @PathVariable("id") Integer todoNo) {
    todoService.deleteTodo(getMemberNo(authentication), todoNo);
    return ResponseEntity.ok(ResponseUtil.success(null));
  }

  @Operation(summary = "Todos 검색", description = "현재 로그인한 회원의 Todo 중에서 키워드로 검색합니다. 키워드가 없으면 모든 Todo를 조회합니다. 정렬 기준: createdAt(생성일), updatedAt(수정일), no, title")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Todo 검색 성공", content = @Content(schema = @Schema(implementation = CustomResponse.class))),
      @ApiResponse(responseCode = "401", description = "인증되지 않은 요청")
  })
  @GetMapping("/search")
  public ResponseEntity<CustomResponse<Page<TodoResponse>>> searchTodos(
      @Parameter(hidden = true) Authentication authentication,
      @Parameter(description = "검색 키워드 (선택사항)", required = false) @RequestParam(required = false) String keyword,
      @Parameter(description = "정렬 기준 (createdAt, updatedAt, no, title)", required = false) @RequestParam(required = false, defaultValue = "createdAt") String sortBy,
      @Parameter(description = "정렬 방향 (asc, desc)", required = false) @RequestParam(required = false, defaultValue = "desc") String direction,
      @PageableDefault(size = 10) Pageable pageable) {
    Sort sort = Sort.by(Sort.Direction.fromString(direction), sortBy);
    Pageable sortedPageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);
    return ResponseEntity
        .ok(ResponseUtil.success(todoService.searchTodos(getMemberNo(authentication), keyword, sortedPageable)));
  }

  private Integer getMemberNo(Authentication authentication) {
    if (authentication == null || !authentication.isAuthenticated()) {
      throw new IllegalStateException("인증되지 않은 사용자입니다.");
    }
    return jwtUtil.getMemberNoFromToken(authentication.getPrincipal().toString());
  }
}