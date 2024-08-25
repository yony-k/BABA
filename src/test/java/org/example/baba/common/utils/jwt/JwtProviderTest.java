package org.example.baba.common.utils.jwt;

import java.nio.charset.StandardCharsets;
import java.util.Collection;

import javax.crypto.SecretKey;

import org.assertj.core.api.SoftAssertions;
import org.example.baba.common.security.details.AuthUser;
import org.example.baba.domain.Member;
import org.example.baba.domain.enums.MemberRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Import(JwtProvider.class)
@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
class JwtProviderTest {

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JwtProperties properties;

  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  @Autowired
  private JwtProvider provider;

  private AuthUser authUser;

  @BeforeEach
  void init() {
    Member tester =
        Member.builder()
            .email("test@gmail.com")
            .memberId(1L)
            .password("1q2w3e4r5ty6")
            .memberRole(MemberRole.USER)
            .build();
    authUser = new AuthUser(tester);
  }

  @Test
  @DisplayName("AccessToken 구현 테스트")
  void generate_access_token_test() throws Exception {
    // given
    String accessToken =
        provider.generateAccessToken(
            authUser.getUsername(), authUser.getId(), toTrans(authUser.getAuthorities()));
    // when
    Claims payload =
        Jwts.parser()
            .verifyWith(getEncodedKey())
            .build()
            .parseSignedClaims(accessToken)
            .getPayload();

    // then
    SoftAssertions.assertSoftly(
        softAssertions -> {
          softAssertions.assertThat(payload.getSubject()).isEqualTo(authUser.getUsername());
          softAssertions.assertThat(payload.get("id", Long.class)).isEqualTo(authUser.getId());
        });
  }

  @Test
  @DisplayName("RefreshToken 구현 테스트")
  void generate_refresh_token_test() throws Exception {
    // given
    String refresh = provider.generateRefreshToken(authUser.getUsername());

    // when
    Claims payload =
        Jwts.parser().verifyWith(getEncodedKey()).build().parseSignedClaims(refresh).getPayload();
    // then
    SoftAssertions.assertSoftly(
        softAssertions -> {
          softAssertions.assertThat(payload.getSubject()).isEqualTo(authUser.getUsername());
        });
  }

  private SecretKey getEncodedKey() {
    return Keys.hmacShaKeyFor(properties.getSecretKey().getBytes(StandardCharsets.UTF_8));
  }

  private String toTrans(Collection<GrantedAuthority> list) {
    StringBuilder sb = new StringBuilder();
    list.forEach(data -> sb.append(data).append(","));
    return sb.deleteCharAt(sb.length() - 1).toString();
  }
}
