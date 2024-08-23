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
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class VerificationAccessDeniedHandler implements AccessDeniedHandler {
  private final ObjectMapperUtils objectMapper;

  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    log.error("No Authorities", accessDeniedException);
    log.error("Request Uri : {}", request.getRequestURI());

    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put(
        "status", AuthorizedExceptionType.UNAUTHENTICATED.getStatus().getReasonPhrase());
    errorResponse.put("message", AuthorizedExceptionType.UNAUTHENTICATED.getMessage());

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.FORBIDDEN.value());
    response.setCharacterEncoding("UTF-8");
    response.getWriter().write(objectMapper.toStringValue(errorResponse));
  }
}
