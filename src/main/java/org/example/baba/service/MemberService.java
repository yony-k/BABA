package org.example.baba.service;

import java.security.SecureRandom;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.domain.ApprovalCode;
import org.example.baba.domain.Register;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.RegisterExceptionType;
import org.example.baba.repository.ApprovalCodeRepository;
import org.example.baba.repository.MemberRepository;
import org.example.baba.repository.RegisterRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    mailService.sendEmail(registerDTO.getEmail(), randomCode);
  }

  public void confirmApprovalCode(String approvalCode) {
    // 가입승인 코드 검증 및 정식 회원가입
    safeStoreService.confirmApprovalCode(approvalCode);
  }
}
