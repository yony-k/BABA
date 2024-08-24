package org.example.baba.common.security.handler;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class AuthenticationEntryPointHandler implements AuthenticationEntryPoint {

  private final ObjectMapperUtils objectMapper;

  @Override
  public void commence(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException)
      throws IOException {
    sendError(response, getExceptionCodeByRequest(request));
  }

  private AuthorizedExceptionType getExceptionCodeByRequest(HttpServletRequest request) {
    if (!(request.getAttribute("exceptionCode") instanceof AuthorizedExceptionType)) {
      return AuthorizedExceptionType.UNAUTHENTICATED;
    }
    return (AuthorizedExceptionType) request.getAttribute("exceptionCode");
  }

  private void sendError(HttpServletResponse response, AuthorizedExceptionType exceptionCode)
      throws IOException {
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    response.setStatus(exceptionCode.getStatus().value());
    response.getWriter().write(getResponseData(exceptionCode));
  }

  private String getResponseData(AuthorizedExceptionType exceptionCode) {
    return objectMapper.toStringValue(createErrorResponse(exceptionCode));
  }

  private Map<String, String> createErrorResponse(AuthorizedExceptionType exceptionCode) {
    Map<String, String> errorResponse = new HashMap<>();
    errorResponse.put("status", exceptionCode.getStatus().name());
    errorResponse.put("message", exceptionCode.getMessage());
    return errorResponse;
  }
}
