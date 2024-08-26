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

  private final MemberRepository memberRepository;
  private final RegisterRepository registerRepository;
  private final ApprovalCodeRepository approvalCodeRepository;
  private final JavaMailSender mailSender;

  private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
  private static final int CODE_LENGTH = 6;
  private static final SecureRandom random = new SecureRandom();

  @Value("${spring.mail.username}")
  private String senderEmail;

  public static final String MEMBERKEY_PREFIX = "temporary:";
  public static final String APPROVALKEY_PREFIX = "approval:";

  // DB에서 중복 계정명, 중복 이메일 검사
  public void existInDB(RegisterDTO registerDTO) {
    // 중복 계정명 검사
    boolean existByMemberName = memberRepository.existsByMemberName(registerDTO.getMemberName());
    // 중복 이메일 검사
    boolean existByEmai = memberRepository.existsByEmail(registerDTO.getEmail());

    // 중복일 시 예외 발생
    if (existByMemberName) throw new CustomException(RegisterExceptionType.DUPLICATED_MEMBER_NAME);
    if (existByEmai) throw new CustomException(RegisterExceptionType.DUPLICATED_EMAIL);
  }

  // Redis에 임시저장된 사용자 정보가 있는지 검사
  public void existInRedis(RegisterDTO registerDTO) {
    // 중복 계정명 검사
    boolean existByMemberName = registerRepository.existsByMemberName(registerDTO.getMemberName());
    // 중복 이메일 검색
    boolean existById = registerRepository.existsById(MEMBERKEY_PREFIX + registerDTO.getEmail());

    // 중복일 시 예외 발생
    if (existByMemberName) throw new CustomException(RegisterExceptionType.DUPLICATED_MEMBER_NAME);
    if (existById) throw new CustomException(RegisterExceptionType.EMAIL_ALREADY_IN_PROGRESS);
  }

  // 임시 회원가입 및 가입승인 코드 발생
  @Transactional
  public void sendApprovalCode(RegisterDTO registerDTO) {
    // 가입승인 코드 생성
    String randomCode = generateRandomCode();
    // 가입승인 코드 엔티티 생성
    ApprovalCode approvalCode =
        ApprovalCode.builder()
            .approvalKey(APPROVALKEY_PREFIX + randomCode)
            .email(registerDTO.getEmail())
            .build();

    // 사용자 정보 임시 저장
    registerRepository.save(registerDTO.toEntity(MEMBERKEY_PREFIX));
    // 가입승인 코드 저장
    approvalCodeRepository.save(approvalCode);

    // 가입승인 코드를 포함한 이메일 전송
    sendEmail(registerDTO.getEmail(), randomCode);
    log.info("가입승인 코드 이메일 발송: {}, 가입승인 코드: {}", registerDTO.getEmail(), randomCode);
  }

  // 가입승인 코드 생성
  public String generateRandomCode() {
    // StringBuilder 에 문자열 크기 지정해주며 생성
    StringBuilder randomCode = new StringBuilder(CHARACTERS.length());
    // 랜덤하게 CHARACTERS 의 인덱스를 뽑고 인덱스를 이용하여 연결된 문자열 생성
    for (int i = 0; i < CODE_LENGTH; i++) {
      int index = random.nextInt(CHARACTERS.length());
      randomCode.append(CHARACTERS.charAt(index));
    }
    return randomCode.toString();
  }

  // 가입승인 메일 전송
  public void sendEmail(String email, String randomCode) {
    MimeMessage mimeMessage = createMessage(email, randomCode);
    mailSender.send(mimeMessage);
  }

  // 메일 내용 생성
  public MimeMessage createMessage(String email, String randomCode) {
    MimeMessage mimeMessage = mailSender.createMimeMessage();
    try {
      MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
      // 보내야할 이메일 설정
      mimeMessageHelper.setTo(email);
      // 보내는 이메일 설정
      mimeMessageHelper.setFrom(senderEmail);
      // 메일 제목 설정
      mimeMessageHelper.setSubject("가입승인 코드");
      // 메일 본문 작성
      String body = "";
      body += "<h3>" + "요청하신 인증 번호입니다." + "</h3>";
      body += "<h1>" + randomCode + "</h1>";
      body += "<h3>" + "감사합니다." + "</h3>";
      // 메일 본문 설정
      mimeMessageHelper.setText(body, true);
    } catch (MessagingException e) {
      throw new RuntimeException(e);
    }
    return mimeMessage;
  }

  // 가입승인 코드 검증 및 정식 회원가입
  @Transactional
  public void confirmApprovalCode(String approvalCode) {
    // 가입승인 코드로 redis에 저장된 이메일 가져오기
    ApprovalCode savedApproval =
        approvalCodeRepository
            .findById(APPROVALKEY_PREFIX + approvalCode)
            .orElseThrow(() -> new CustomException(RegisterExceptionType.NOT_FOUND_CODE));

    // 가입승인 코드와 함께 저장된 이메일을 키로 저장된 임시 사용자 정보 가져오기
    Register savedRegister =
        registerRepository
            .findById(MEMBERKEY_PREFIX + savedApproval.getEmail())
            .orElseThrow(() -> new CustomException(RegisterExceptionType.NOT_FOUND_MEMBER));

    // 가입승인 코드와 함께 저장된 이메일
    String approvalEmail = savedApproval.getEmail();
    // 임시 저장 된 사용자 이메일
    String registerEmail = savedRegister.getEmail().substring(MEMBERKEY_PREFIX.length());

    // 가입승인 코드와 함께 저장된 이메일과 임시 사용자 정보의 이메일 비교 후 정식 회원가입
    if (approvalEmail.equals(registerEmail)) {
      memberRepository.save(savedRegister.toMember(MEMBERKEY_PREFIX.length()));
      registerRepository.delete(savedRegister);
      approvalCodeRepository.delete(savedApproval);
    } else {
      log.error(
          "코드 발송한 이메일과 임시 저장된 이메일 불일치: code({}), savedEmail({}), Register Email({})",
          approvalCode,
          approvalEmail,
          registerEmail);
      throw new CustomException(RegisterExceptionType.DATA_CONSISTENCY_ERROR);
    }
  }
}
