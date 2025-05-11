package com.todo.api.mmbr.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class MemberUpdateRequest {
  @Size(min = 2, max = 100, message = "이름은 2자 이상 100자 이하로 입력해주세요.")
  private String name;

  @Size(min = 8, max = 255, message = "비밀번호는 8자 이상 255자 이하로 입력해주세요.")
  private String password;
}