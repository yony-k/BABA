package org.example.baba.common.utils.cookie;

import java.util.*;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CookieUtils {

  private final CookieProperties cookieProperties;

  // 쿠키 생성 후 반환
  public Cookie createCookie(String key) {
    Cookie cookie = new Cookie(cookieProperties.getCookieName(), key);
    cookie.setHttpOnly(true);
    // cookie.setSecure(true);
    cookie.setDomain(cookieProperties.getDomain());
    //    cookie.setPath(cookieProperties.getAcceptedUrl());
    cookie.setMaxAge(60 * cookieProperties.getLimitTime());
    return cookie;
  }

  // 쿠키 삭제
  public Cookie deleteCookie(HttpServletRequest request) {
    Cookie cookie = searchCookieProperties(validCookiesExist(request));
    cookie.setMaxAge(0);
    return cookie;
  }

  // 쿠키 배열 반환
  private Cookie[] validCookiesExist(HttpServletRequest request) {
    return Optional.ofNullable(request.getCookies()).orElse(new Cookie[] {});
  }

  // 일치하는 쿠키 조회
  private Cookie searchCookieProperties(Cookie[] cookies) {
    return Arrays.stream(cookies)
        .filter(cookie -> cookie.getName().equals(cookieProperties.getCookieName()))
        .findFirst()
        .orElse(new Cookie(cookieProperties.getCookieName(), ""));
  }

  public Cookie searchCookieProperties(HttpServletRequest request) {
    return Arrays.stream(validCookiesExist(request))
        .filter(cookie -> cookie.getName().equals(cookieProperties.getCookieName()))
        .findFirst()
        .orElseThrow(() -> new CustomException(AuthorizedExceptionType.REFRESH_TOKEN_EXPIRED));
  }
}
