package com.todo.api.todo.repository;

import com.todo.api.todo.domain.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TodoRepository extends JpaRepository<Todo, Integer> {
  Page<Todo> findByMemberNo(Integer memberNo, Pageable pageable);

  @Query("SELECT t FROM Todo t WHERE t.member.no = :memberNo AND " +
      "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
      "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
  Page<Todo> searchByKeyword(@Param("memberNo") Integer memberNo, @Param("keyword") String keyword, Pageable pageable);
}