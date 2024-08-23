package org.example.baba.service;

import java.security.SecureRandom;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.RegisterExceptionType;
import org.example.baba.repository.MemberRepository;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberService {

  private final MemberRepository memberRepository;
  private final RedisTemplate<String, Object> redisTemplate;
  private ObjectMapper objectMapper;

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int CODE_LENGTH = 6;
  private static final SecureRandom random = new SecureRandom();

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

  // 임시 회원가입 및 가입승인 코드 발생
  public void sendApprovalCode(RegisterDTO registerDTO) {
    // 임시 사용자 정보 키
    String memberKey = "temporary:" + registerDTO.getEmail();

    // 가입승인 코드 생성
    String code = generateRandomCode();

    // 가입승인 코드 키
    String approvalKey = "approval:" + code;

    // 트랜잭션
    redisTemplate.execute(
        new SessionCallback<Object>() {
          @Override
          public Object execute(RedisOperations operations) {
            operations.multi();
            try {
              // redis에 임시 사용자 정보 저장
              operations.opsForValue().set(memberKey, registerDTO.toJson());
              // redis에 가입승인 코드 저장
              operations.opsForValue().set(approvalKey, registerDTO.getEmail());
              return operations.exec();
            } catch (Exception e) {
              operations.discard();
              throw new RuntimeException("sendApprovalCode 메소드 redis 실행 실패", e);
            }
          }
        });
    // 가입승인 코드를 포함한 이메일 전송
  }

  public static String generateRandomCode() {
    // StringBuilder 에 문자열 크기 지정해주며 생성
    StringBuilder code = new StringBuilder(CHARACTERS.length());

    // 랜덤하게 CHARACTERS 의 인덱스를 뽑고 인덱스를 이용하여 연결된 문자열 생성
    for (int i = 0; i < CODE_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      code.append(CHARACTERS.charAt(index));
    }
    return code.toString();
  }
}
