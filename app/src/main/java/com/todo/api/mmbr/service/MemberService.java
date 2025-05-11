package com.todo.api.mmbr.service;

import com.todo.api.mmbr.dto.*;

public interface MemberService {

  MemberDto.SignUpResponse signUp(MemberDto.SignUpRequest request);

  MemberDto.LoginResponse login(MemberDto.LoginRequest request);

  MemberDto.InfoResponse getCurrentMember();

  MemberDto.InfoResponse updateCurrentMember(MemberDto.UpdateRequest request);

  void deleteCurrentMember();
}