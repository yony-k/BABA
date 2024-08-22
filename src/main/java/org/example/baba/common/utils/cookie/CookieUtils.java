package org.example.baba.common.utils.cookie;

import jakarta.servlet.http.Cookie;

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
    cookie.setPath(cookieProperties.getAcceptedUrl());
    cookie.setMaxAge(60 * cookieProperties.getLimitTime());
    return cookie;
  }
}
