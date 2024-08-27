package org.example.baba.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.mail.internet.MimeMessage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.mail.javamail.JavaMailSender;

public class MailServiceTest {

  @Mock private JavaMailSender mailSender;

  @InjectMocks private MailService mailService;

  @Test
  @DisplayName("메일 생성 테스트")
  public void createMessageTest() {
    // given
    String email = "minji12@gmail.com";
    String randomCode = "123456";

    // when
    MimeMessage mimeMessage = mailService.createMessage(email, randomCode);

    // then
    // mimeMessage가 생성되었는지 확인
    assertNotNull(mimeMessage);
    try {
      // 받는 사람 이메일 확인
      assertEquals(email, mimeMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
      // 메일 제목 확인
      assertEquals("가입승인 코드", mimeMessage.getSubject());
      // 메일 내용에 가입승인 코드 포함되어있는지 확인
      assertTrue(mimeMessage.getContent().toString().contains(randomCode));
    } catch (Exception e) {
      fail("메일 생성 오류: " + e.getMessage());
    }
  }

  @Test
  @DisplayName("메일 전송 테스트")
  public void sendEmail() {
    // given
    String email = "minji12@gmail.com";
    String randomCode = "123456";

    // MimeMessage 의 모의객체를 만들어서 createMimeMessage 메소드가 호출될 때 리턴값 지정
    MimeMessage mimeMessage = mock(MimeMessage.class);
    when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

    // when
    mailService.sendCode(email, randomCode);

    // then
    // 메소드 호출 시 imeMessage 타입의 인자가 들어오면 캡처하고 검증하도록 도와주는 클래스 설정
    ArgumentCaptor<MimeMessage> mimeMessageCaptor = ArgumentCaptor.forClass(MimeMessage.class);
    // mailSender의 send 메소드 호출 확인 & 사용되는 인자 캡처
    verify(mailSender).send(mimeMessageCaptor.capture());
    // 캡처된 인자 가져오기
    MimeMessage capturedMimeMessage = mimeMessageCaptor.getValue();

    try {
      assertNotNull(capturedMimeMessage);
      assertEquals(
          email, capturedMimeMessage.getRecipients(MimeMessage.RecipientType.TO)[0].toString());
      assertEquals("가입승인 코드", capturedMimeMessage.getSubject());
      assertTrue(capturedMimeMessage.getContent().toString().contains(randomCode));
    } catch (Exception e) {
      fail("메일 전송 오류: " + e.getMessage());
    }
  }
}
