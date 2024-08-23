package org.example.baba.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.example.baba.controller.dto.request.RegisterDTO;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.RegisterExceptionType;
import org.example.baba.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class RegisterServiceTest {

  @Mock private MemberRepository memberRepository;

  @InjectMocks private MemberService memberService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  @DisplayName("중복된 계정명 실패 케이스")
  public void duplicatedMemberName() {

    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder().memberName("김민지").password("1234").email("minji12@gmail.com").build();

    // existsByMemberName의 메소드 사용시 리턴값 설정
    when(memberRepository.existsByMemberName("김민지")).thenReturn(true);

    // when & then
    // memberService의 메소드 사용시 발생할 예외 클래스 확인
    CustomException thrown =
        assertThrows(
            CustomException.class,
            () -> {
              memberService.isDuplicated(registerDTO);
            });

    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.DUPLICATED_MEMBER_NAME.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("중복된 이메일 실패 케이스")
  public void duplicatedEmail() {

    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder().memberName("김성민").password("1234").email("minji@gmail.com").build();

    // existsByMemberName의 메소드 사용시 리턴값 설정
    when(memberRepository.existsByEmail("minji@gmail.com")).thenReturn(true);

    // when & then
    // memberService의 메소드 사용시 발생할 예외 클래스 확인
    CustomException thrown =
        assertThrows(
            CustomException.class,
            () -> {
              memberService.isDuplicated(registerDTO);
            });

    // 예외 메세지 확인
    assertEquals(RegisterExceptionType.DUPLICATED_EMAIL.getMessage(), thrown.getMessage());
  }

  @Test
  @DisplayName("중복되지 않은 계정명, 이메일 성공 케이스")
  public void noDuplicateNameAndEmail() {
    // given
    // RegisterDTO 생성
    RegisterDTO registerDTO =
        RegisterDTO.builder().memberName("김성민").password("1234").email("sungmin@gmail.com").build();

    // existsByMemberName, existsByEmail 사용 시 리턴값 지정
    when(memberRepository.existsByMemberName("김성민")).thenReturn(false);
    when(memberRepository.existsByEmail("sungmin@gmail.com")).thenReturn(false);

    // when
    memberService.isDuplicated(registerDTO);

    // then
    // 아무런 예외가 발생하지 않아야 함
  }
}
