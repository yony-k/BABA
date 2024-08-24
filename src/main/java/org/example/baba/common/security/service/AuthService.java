package org.example.baba.common.security.service;

import java.util.Optional;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.baba.common.redis.RedisRepository;
import org.example.baba.common.security.dto.MemberInfo;
import org.example.baba.common.utils.cookie.CookieUtils;
import org.example.baba.common.utils.jwt.JwtProperties;
import org.example.baba.common.utils.jwt.JwtProvider;
import org.example.baba.common.utils.translator.ObjectMapperUtils;
import org.example.baba.domain.Member;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.example.baba.exception.exceptionType.UserExceptionType;
import org.example.baba.repository.MemberRepository;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final RedisRepository redis;
  private final JwtProvider provider;
  private final JwtProperties properties;
  private final MemberRepository memberRepository;
  private final ObjectMapperUtils objectMapperUtils;
  private final CookieUtils cookieUtils;

  // 리프레시 토큰 재발급
  public void reissue(HttpServletRequest request, HttpServletResponse response) {
    Member findMember = validRefreshTokenSubject(findMemberData(request));

    String refresh = getRefreshToken(findMember);
    String access = getAccessToken(findMember, findMemberData(request));

    saveMemberInfoToRedis(refresh, findMemberData(request));

    response.setHeader(HttpHeaders.AUTHORIZATION, access);
    response.addCookie(cookieUtils.createCookie(refresh));
  }

  private MemberInfo findMemberData(HttpServletRequest request) {
    return findMemberInfo(cookieUtils.searchCookieProperties(request));
  }

  private String getRefreshToken(Member findMember) {
    return provider.generateRefreshToken(findMember.getEmail());
  }

  private String getAccessToken(Member findMember, MemberInfo info) {
    return properties.getPrefix()
        + provider.generateAccessToken(
            findMember.getEmail(), findMember.getMemberId(), info.getAuthorities());
  }

  private void saveMemberInfoToRedis(String refresh, MemberInfo info) {
    redis.save(
        refresh,
        objectMapperUtils.toStringValue(info),
        provider.getRefreshTokenValidityInSeconds());
  }

  private Member validRefreshTokenSubject(MemberInfo memberInfo) {
    return memberRepository
        .findByEmail(memberInfo.getEmail())
        .orElseThrow(() -> new CustomException(UserExceptionType.USER_NOT_FOUND));
  }

  private MemberInfo findMemberInfo(Cookie refreshCookie) {
    return objectMapperUtils.toEntity(findAndDeleteToRedis(refreshCookie), MemberInfo.class);
  }

  private String findAndDeleteToRedis(Cookie refreshCookie) {
    String tokenToRedis = findTokenToRedis(refreshCookie);
    deleteToken(tokenToRedis);
    return tokenToRedis;
  }

  private void deleteToken(String tokenToRedis) {
    redis.delete(tokenToRedis);
  }

  private String findTokenToRedis(Cookie refreshCookie) {
    return Optional.ofNullable(redis.findByKey(refreshCookie.getValue()))
        .orElseThrow(() -> new CustomException(AuthorizedExceptionType.REFRESH_TOKEN_NOT_FOUND));
  }
}
