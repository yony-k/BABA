package org.example.baba.service;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.RegisterExceptionType;
import org.example.baba.repository.MemberRepository;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;

  // 중복 계정명, 중복 이메일 검사
  public void isDuplicated(RegisterDTO registerDTO) {
    // 중복 계정명 검사
    boolean existByMemberName = memberRepository.existsByMemberName(registerDTO.getMemberName());
    // 중복 이메일 검사
    boolean existByEmail = memberRepository.existsByEmail(registerDTO.getEmail());

    // 중복일 시 예외 발생
    if (existByMemberName) throw new CustomException(RegisterExceptionType.DUPLICATED_MEMBER_NAME);
    if (existByEmail) throw new CustomException(RegisterExceptionType.DUPLICATED_EMAIL);
  }
}
