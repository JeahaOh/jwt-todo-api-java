package com.todo.api.mmbr.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "member",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "email")
        }
)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Member {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer no;

        @Column(nullable = false, unique = true, length = 255)
        private String email;

        @Column(nullable = false, length = 100)
        private String name;

        @Column(nullable = false, length = 255)
        private String password;

        @CreationTimestamp
        @Column(updatable = false)
        private LocalDateTime createdAt;

        @UpdateTimestamp
        private LocalDateTime updatedAt;
}
