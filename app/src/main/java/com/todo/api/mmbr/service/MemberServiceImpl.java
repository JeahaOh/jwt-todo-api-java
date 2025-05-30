package com.todo.api.mmbr.service;

import com.todo.api.common.constant.ErrorCode;
import com.todo.api.common.exception.CustomException;
import com.todo.api.common.util.JwtUtil;
import com.todo.api.mmbr.domain.Member;
import com.todo.api.mmbr.dto.*;
import com.todo.api.mmbr.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    @Transactional
    public MemberDto.SignUpResponse signUp(MemberDto.SignUpRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new CustomException(ErrorCode.MEMBER_EMAIL_DUPLICATE);
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword());

        Member member = new Member();
        member.setEmail(request.getEmail());
        member.setName(request.getName());
        member.setPassword(encodedPassword);

        Member savedMember = memberRepository.save(member);

        return MemberDto.SignUpResponse.builder()
                .no(savedMember.getNo())
                .email(savedMember.getEmail())
                .name(savedMember.getName())
                .build();
    }

    @Override
    public MemberDto.LoginResponse login(MemberDto.LoginRequest request) {
        Member member = memberRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new CustomException(ErrorCode.MEMBER_PASSWORD_MISMATCH);
        }

        String accessToken = jwtUtil.generateToken(member.getEmail(), member.getNo());

        return MemberDto.LoginResponse.builder()
                .accessToken(accessToken)
                .user(MemberDto.LoginResponse.UserInfo.builder()
                        .no(member.getNo())
                        .email(member.getEmail())
                        .name(member.getName())
                        .createdAt(member.getCreatedAt())
                        .updatedAt(member.getUpdatedAt())
                        .build())
                .build();
    }

    @Override
    public MemberDto.InfoResponse getCurrentMember() {
        String jwt = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String email = jwtUtil.getEmailFromToken(jwt);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberDto.InfoResponse.from(member);
    }

    @Override
    @Transactional
    public MemberDto.InfoResponse updateCurrentMember(MemberDto.UpdateRequest request) {
        String jwt = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String email = jwtUtil.getEmailFromToken(jwt);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        if (request.getName() == null && request.getPassword() == null) {
            throw new CustomException(ErrorCode.MEMBER_UPDATE_NO_DATA);
        }

        if (request.getName() != null) {
            member.setName(request.getName());
        }
        if (request.getPassword() != null) {
            member.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        member.setUpdatedAt(LocalDateTime.now());

        Member updatedMember = memberRepository.save(member);

        return MemberDto.InfoResponse.from(updatedMember);
    }

    @Override
    @Transactional
    public void deleteCurrentMember() {
        String jwt = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        String email = jwtUtil.getEmailFromToken(jwt);
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        memberRepository.delete(member);
    }
}