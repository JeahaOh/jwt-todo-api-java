package com.todo.api.mmbr.dto;

import com.todo.api.mmbr.domain.Member;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class MemberDto {
  @Getter
  @Setter
  public static class SignUpRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "이름은 필수 입력값입니다.")
    @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해주세요.")
    private String name;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    @Size(min = 8, max = 255, message = "비밀번호는 8자 이상 255자 이하로 입력해주세요.")
    private String password;
  }

  @Getter
  @Builder
  public static class SignUpResponse {
    private Integer no;
    private String email;
    private String name;
  }

  @Getter
  @Setter
  public static class LoginRequest {
    @NotBlank(message = "이메일은 필수 입력값입니다.")
    @Email(message = "올바른 이메일 형식이 아닙니다.")
    private String email;

    @NotBlank(message = "비밀번호는 필수 입력값입니다.")
    private String password;
  }

  @Getter
  @Builder
  public static class LoginResponse {
    private String accessToken;
    private UserInfo user;

    @Getter
    @Builder
    public static class UserInfo {
      private Integer no;
      private String email;
      private String name;
      private LocalDateTime createdAt;
      private LocalDateTime updatedAt;
    }
  }

  @Getter
  @Setter
  @Builder
  public static class InfoResponse {
    private Integer no;
    private String email;
    private String name;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static InfoResponse from(Member member) {
      return InfoResponse.builder()
          .no(member.getNo())
          .email(member.getEmail())
          .name(member.getName())
          .createdAt(member.getCreatedAt())
          .updatedAt(member.getUpdatedAt())
          .build();
    }
  }

  @Getter
  @Setter
  public static class UpdateRequest {
    @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해주세요.")
    private String name;

    @Size(min = 8, max = 255, message = "비밀번호는 8자 이상 255자 이하로 입력해주세요.")
    private String password;
  }
}