package org.example.baba.controller;

import jakarta.validation.Valid;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService memberService;

  // 가입승인 요청 및 임시 회원가입
  @PostMapping
  public ResponseEntity<String> registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
    // DB에서 중복 검사
    memberService.existInDB(registerDTO);
    // Redis에서 중복 검사
    memberService.existInRedis(registerDTO);
    // 가입승인 코드 전송
    memberService.sendApprovalCode(registerDTO);
    return ResponseEntity.ok("인증 코드가 발송되었습니다.");
  }

  // 가입승인 코드 확인 및 정식 회원가입
  @PostMapping("/confirm")
  public ResponseEntity<String> registerConfirm(@RequestParam("code") String approvalCode) {
    // 가입승인 및 정식 회원가입
    memberService.confirmApprovalCode(approvalCode);
    return ResponseEntity.ok("회원가입이 성공적으로 완료되었습니다.");
  }
}
