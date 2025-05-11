package com.todo.api.todo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "todo")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Todo {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer no;

  @Column(nullable = false, length = 255)
  private String title;

  @Column(length = 1000)
  private String description;

  @Column(nullable = false)
  private boolean completed = false;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "member_no", nullable = false)
  private com.todo.api.mmbr.domain.Member member;

  @CreationTimestamp
  @Column(updatable = false)
  private LocalDateTime createdAt;

  @UpdateTimestamp
  private LocalDateTime updatedAt;
}