package org.example.baba.service;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
  private final SafeStoreService safeStoreService;
  private final MailService mailService;

  public void sendApprovalCode(RegisterDTO registerDTO) {
    // DB, Redis 중복 검증
    safeStoreService.existInDB(registerDTO);
    safeStoreService.existInRedis(registerDTO);
    // 가입승인 코드 생성
    String randomCode = safeStoreService.generateRandomCode();
    // 임시 사용자 정보 및 가입승인 코드 Redis에 저장
    safeStoreService.tempRegister(registerDTO, randomCode);
    // 가입승인 코드 메일 전송
    mailService.sendCode(registerDTO.getEmail(), randomCode);
  }

  public void confirmApprovalCode(String approvalCode) {
    // 가입승인 코드 검증 및 정식 회원가입
    safeStoreService.confirmApprovalCode(approvalCode);
  }
}
