package com.todo.api.todo.controller;

import com.todo.api.common.util.JwtUtil;
import com.todo.api.todo.dto.TodoCreateRequest;
import com.todo.api.todo.dto.TodoResponse;
import com.todo.api.todo.dto.TodoUpdateRequest;
import com.todo.api.todo.service.TodoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TodoController.class)
@ActiveProfiles("test")
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TodoService todoService;

    @MockBean
    private JwtUtil jwtUtil;

    @Test
    @DisplayName("Todo 생성 API가 정상적으로 동작하는지 확인")
    @WithMockUser
    void createTodo() throws Exception {
        // given
        Integer memberNo = 1;
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("테스트 제목");
        request.setDescription("테스트 설명");

        TodoResponse response = new TodoResponse();
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setCompleted(false);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.createTodo(eq(memberNo), any(TodoCreateRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(post("/todos")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).createTodo(eq(memberNo), any(TodoCreateRequest.class));
    }

    @Test
    @DisplayName("Todo 목록 조회 API가 정상적으로 동작하는지 확인")
    @WithMockUser
    void getTodos() throws Exception {
        // given
        Integer memberNo = 1;
        Pageable pageable = PageRequest.of(0, 10);

        TodoResponse todo1 = new TodoResponse();
        todo1.setTitle("제목1");
        todo1.setDescription("설명1");
        todo1.setCompleted(false);

        TodoResponse todo2 = new TodoResponse();
        todo2.setTitle("제목2");
        todo2.setDescription("설명2");
        todo2.setCompleted(true);

        List<TodoResponse> todos = Arrays.asList(todo1, todo2);
        Page<TodoResponse> todoPage = new PageImpl<>(todos);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.getTodos(eq(memberNo), any(Pageable.class))).thenReturn(todoPage);

        // when & then
        mockMvc.perform(get("/todos")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).getTodos(eq(memberNo), any(Pageable.class));
    }

    @Test
    @DisplayName("Todo 수정 API가 정상적으로 동작하는지 확인")
    @WithMockUser
    void updateTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;
        TodoUpdateRequest request = new TodoUpdateRequest();
        request.setTitle("수정된 제목");
        request.setDescription("수정된 설명");
        request.setCompleted(true);

        TodoResponse response = new TodoResponse();
        response.setTitle(request.getTitle());
        response.setDescription(request.getDescription());
        response.setCompleted(request.isCompleted());

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.updateTodo(eq(memberNo), eq(todoNo), any(TodoUpdateRequest.class))).thenReturn(response);

        // when & then
        mockMvc.perform(put("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).updateTodo(eq(memberNo), eq(todoNo), any(TodoUpdateRequest.class));
    }

    @Test
    @DisplayName("Todo 삭제 API가 정상적으로 동작하는지 확인")
    @WithMockUser
    void deleteTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        doNothing().when(todoService).deleteTodo(eq(memberNo), eq(todoNo));

        // when & then
        mockMvc.perform(delete("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true));

        verify(todoService).deleteTodo(eq(memberNo), eq(todoNo));
    }

    @Test
    @DisplayName("Todo 검색 API가 정상적으로 동작하는지 확인")
    @WithMockUser
    void searchTodos() throws Exception {
        // given
        Integer memberNo = 1;
        String keyword = "테스트";
        Pageable pageable = PageRequest.of(0, 10);

        TodoResponse todo1 = new TodoResponse();
        todo1.setTitle("테스트 제목1");
        todo1.setDescription("설명1");
        todo1.setCompleted(false);

        TodoResponse todo2 = new TodoResponse();
        todo2.setTitle("테스트 제목2");
        todo2.setDescription("설명2");
        todo2.setCompleted(true);

        List<TodoResponse> todos = Arrays.asList(todo1, todo2);
        Page<TodoResponse> todoPage = new PageImpl<>(todos);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.searchTodos(eq(memberNo), eq(keyword), any(Pageable.class))).thenReturn(todoPage);

        // when & then
        mockMvc.perform(get("/todos/search")
                        .param("keyword", keyword)
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).searchTodos(eq(memberNo), eq(keyword), any(Pageable.class));
    }

    @Test
    @DisplayName("인증되지 않은 사용자로 API 호출 시 예외 발생")
    void unauthorizedAccess() throws Exception {
        // given
        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(null);

        // when & then
        mockMvc.perform(get("/todos")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc")
                        .header("Accept", MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 Todo 생성 시도 시 404 예외가 발생한다")
    @WithMockUser
    void createTodoWithNonExistentUser() throws Exception {
        // given
        Integer memberNo = 999;
        TodoCreateRequest request = new TodoCreateRequest();
        request.setTitle("테스트 제목");
        request.setDescription("테스트 설명");

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.createTodo(eq(memberNo), any(TodoCreateRequest.class)))
                .thenThrow(new RuntimeException("회원을 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(post("/todos")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("특정 Todo를 정상적으로 조회할 수 있다")
    @WithMockUser
    void getTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;

        TodoResponse response = new TodoResponse();
        response.setTitle("테스트 제목");
        response.setDescription("테스트 설명");
        response.setCompleted(false);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.getTodo(eq(memberNo), eq(todoNo))).thenReturn(response);

        // when & then
        mockMvc.perform(get("/todos/{id}", todoNo))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).getTodo(eq(memberNo), eq(todoNo));
    }

    @Test
    @DisplayName("존재하지 않는 Todo를 조회할 경우 404 예외가 발생한다")
    @WithMockUser
    void getNonExistentTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 999;

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.getTodo(eq(memberNo), eq(todoNo)))
                .thenThrow(new RuntimeException("Todo를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(get("/todos/{id}", todoNo))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("다른 사용자의 Todo를 조회할 경우 403 예외가 발생한다")
    @WithMockUser
    void getOtherUserTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.getTodo(eq(memberNo), eq(todoNo)))
                .thenThrow(new RuntimeException("접근 권한이 없습니다."));

        // when & then
        mockMvc.perform(get("/todos/{id}", todoNo))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("다른 사용자의 Todo를 수정할 경우 403 예외가 발생한다")
    @WithMockUser
    void updateOtherUserTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;
        TodoUpdateRequest request = new TodoUpdateRequest();
        request.setTitle("수정된 제목");
        request.setDescription("수정된 설명");
        request.setCompleted(true);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.updateTodo(eq(memberNo), eq(todoNo), any(TodoUpdateRequest.class)))
                .thenThrow(new RuntimeException("수정 권한이 없습니다."));

        // when & then
        mockMvc.perform(put("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("존재하지 않는 Todo를 수정할 경우 404 예외가 발생한다")
    @WithMockUser
    void updateNonExistentTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 999;
        TodoUpdateRequest request = new TodoUpdateRequest();
        request.setTitle("수정된 제목");
        request.setDescription("수정된 설명");
        request.setCompleted(true);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.updateTodo(eq(memberNo), eq(todoNo), any(TodoUpdateRequest.class)))
                .thenThrow(new RuntimeException("Todo를 찾을 수 없습니다."));

        // when & then
        mockMvc.perform(put("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("다른 사용자의 Todo를 삭제할 경우 403 예외가 발생한다")
    @WithMockUser
    void deleteOtherUserTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 1;

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        doThrow(new RuntimeException("삭제 권한이 없습니다."))
                .when(todoService).deleteTodo(eq(memberNo), eq(todoNo));

        // when & then
        mockMvc.perform(delete("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("존재하지 않는 Todo를 삭제할 경우 404 예외가 발생한다")
    @WithMockUser
    void deleteNonExistentTodo() throws Exception {
        // given
        Integer memberNo = 1;
        Integer todoNo = 999;

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        doThrow(new RuntimeException("Todo를 찾을 수 없습니다."))
                .when(todoService).deleteTodo(eq(memberNo), eq(todoNo));

        // when & then
        mockMvc.perform(delete("/todos/{id}", todoNo)
                        .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("검색 키워드가 없을 경우 전체 Todo가 반환된다")
    @WithMockUser
    void searchTodosWithoutKeyword() throws Exception {
        // given
        Integer memberNo = 1;
        Pageable pageable = PageRequest.of(0, 10);

        TodoResponse todo1 = new TodoResponse();
        todo1.setTitle("제목1");
        todo1.setDescription("설명1");
        todo1.setCompleted(false);

        TodoResponse todo2 = new TodoResponse();
        todo2.setTitle("제목2");
        todo2.setDescription("설명2");
        todo2.setCompleted(true);

        List<TodoResponse> todos = Arrays.asList(todo1, todo2);
        Page<TodoResponse> todoPage = new PageImpl<>(todos);

        when(jwtUtil.getMemberNoFromToken(any())).thenReturn(memberNo);
        when(todoService.searchTodos(eq(memberNo), eq(null), any(Pageable.class))).thenReturn(todoPage);

        // when & then
        mockMvc.perform(get("/todos/search")
                        .param("sortBy", "createdAt")
                        .param("direction", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").exists());

        verify(todoService).searchTodos(eq(memberNo), eq(null), any(Pageable.class));
    }
}
