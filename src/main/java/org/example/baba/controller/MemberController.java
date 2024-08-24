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

  @PostMapping("/confirm")
  public ResponseEntity<String> registerConfirm(@RequestParam("code") String code) {

    return null;
  }
}
