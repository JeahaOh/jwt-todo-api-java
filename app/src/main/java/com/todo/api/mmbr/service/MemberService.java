package com.todo.api.mmbr.service;

import com.todo.api.mmbr.dto.*;

public interface MemberService {

  MemberSignUpResponse signUp(MemberSignUpRequest request);

  MemberLoginResponse login(MemberLoginRequest request);

  MemberInfoResponse getCurrentMember();

  MemberInfoResponse updateCurrentMember(MemberUpdateRequest request);

  void deleteCurrentMember();
}