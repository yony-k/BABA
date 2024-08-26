package org.example.baba.common.security.handler;

import java.io.IOException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    AuthorizedExceptionType exceptionCode = getExceptionCodeByRequest(request);
    sendError(response, exceptionCode);
  }

  private AuthorizedExceptionType getExceptionCodeByRequest(HttpServletRequest request) {
    if (!(request.getAttribute("exceptionCode") instanceof AuthorizedExceptionType)) {
      return AuthorizedExceptionType.UNAUTHENTICATED;
    }
    return (AuthorizedExceptionType) request.getAttribute("exceptionCode");
  }

  private void sendError(HttpServletResponse response, AuthorizedExceptionType exceptionCode)
      throws IOException {
    response.setContentType(MediaType.TEXT_PLAIN_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter().write(getResponseData(exceptionCode));
  }

  private String getResponseData(AuthorizedExceptionType exceptionCode) {
    return exceptionCode.getMessage();
  }
}
