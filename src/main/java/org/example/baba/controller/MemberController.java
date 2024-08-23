package org.example.baba.controller;

import jakarta.validation.Valid;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/members")
@RequiredArgsConstructor
public class MemberController {

  private final MemberService registerService;
  private final MemberService memberService;

  @PostMapping
  public ResponseEntity<String> registerRequest(@Valid @RequestBody RegisterDTO registerDTO) {
    memberService.isDuplicated(registerDTO);
    memberService.sendApprovalCode(registerDTO);
    return ResponseEntity.ok("인증 코드가 발송되었습니다.");
  }
}
