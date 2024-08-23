package org.example.baba.common.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthenticationFailureCustomHandler implements AuthenticationFailureHandler {

  private final ObjectMapperUtils objectMapper;

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request, HttpServletResponse response, AuthenticationException exception)
      throws IOException, ServletException {
    Map<String, String> errorResponse =
        createErrorResponse(AuthorizedExceptionType.UNAUTHENTICATED);

    log.info("start AuthenticationFailureCustomHandler");

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.toStringValue(errorResponse));
  }

  private Map<String, String> createErrorResponse(AuthorizedExceptionType exceptionCode) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("status", exceptionCode.getStatus().name());
    errorResponse.put("message", exceptionCode.getMessage());
    return errorResponse;
  }
}
