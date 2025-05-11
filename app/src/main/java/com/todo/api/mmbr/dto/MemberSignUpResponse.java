package com.todo.api.mmbr.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberSignUpResponse {
  private Integer no;
  private String email;
  private String name;
}