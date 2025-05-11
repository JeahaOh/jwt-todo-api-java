package com.todo.api.mmbr.controller;

import com.todo.api.common.CustomResponse;
import com.todo.api.common.util.ResponseUtil;
import com.todo.api.mmbr.dto.MemberLoginRequest;
import com.todo.api.mmbr.dto.MemberLoginResponse;
import com.todo.api.mmbr.dto.MemberSignUpRequest;
import com.todo.api.mmbr.dto.MemberSignUpResponse;
import com.todo.api.mmbr.dto.MemberInfoResponse;
import com.todo.api.mmbr.dto.MemberUpdateRequest;
import com.todo.api.mmbr.service.MemberServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "01. Member", description = "회원 API")
public class MemberController {

  private final MemberServiceImpl memberService;

  @PostMapping("/signup")
  @Operation(summary = "회원 가입", description = "새로운 회원을 등록합니다.")
  public ResponseEntity<CustomResponse<MemberSignUpResponse>> signUp(@Valid @RequestBody MemberSignUpRequest request) {
    MemberSignUpResponse response = memberService.signUp(request);
    return ResponseEntity.ok(ResponseUtil.success(response));
  }

  @PostMapping("/login")
  @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인합니다.")
  public ResponseEntity<CustomResponse<MemberLoginResponse>> login(@Valid @RequestBody MemberLoginRequest request) {
    MemberLoginResponse response = memberService.login(request);
    return ResponseEntity.ok(ResponseUtil.success(response));
  }

  @GetMapping("/me")
  @Operation(summary = "현재 사용자 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
  public ResponseEntity<CustomResponse<MemberInfoResponse>> getCurrentMember() {
    MemberInfoResponse response = memberService.getCurrentMember();
    return ResponseEntity.ok(ResponseUtil.success(response));
  }

  @PutMapping("/me")
  @Operation(summary = "현재 사용자 정보 수정", description = "현재 로그인한 사용자의 정보를 수정합니다.")
  public ResponseEntity<CustomResponse<MemberInfoResponse>> updateCurrentMember(
      @Valid @RequestBody MemberUpdateRequest request) {
    MemberInfoResponse response = memberService.updateCurrentMember(request);
    return ResponseEntity.ok(ResponseUtil.success(response));
  }

  @DeleteMapping("/me")
  @Operation(summary = "현재 사용자 삭제", description = "현재 로그인한 사용자를 삭제합니다.")
  public ResponseEntity<CustomResponse<Void>> deleteCurrentMember() {
    memberService.deleteCurrentMember();
    return ResponseEntity.ok(ResponseUtil.success(null));
  }
}