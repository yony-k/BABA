package org.example.baba.common.security.handler;

import java.io.IOException;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.baba.common.redis.RedisRepository;
import org.example.baba.common.utils.cookie.CookieUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class LogoutSuccessCustomHandler implements LogoutSuccessHandler {

  private final CookieUtils cookieUtils;
  private final RedisRepository repository;

  @Override
  public void onLogoutSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {

    deleteTokenToRedis(cookieUtils.deleteCookie(request));

    createResponse(response);
  }

  public static void createResponse(HttpServletResponse response) throws IOException {
    response.setStatus(HttpStatus.OK.value());
    response.getWriter().flush();
  }

  private void deleteTokenToRedis(Cookie refreshCookie) {
    repository.delete(refreshCookie.getValue());
  }
}
