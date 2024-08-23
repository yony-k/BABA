package org.example.baba.common.security.details;

import org.example.baba.domain.Member;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import io.jsonwebtoken.Claims;
import lombok.Getter;

@Getter
public class AuthUser extends User {

  private final Long id;

  public AuthUser(Member member) {
    super(
        member.getEmail(),
        member.getPassword(),
        AuthorityUtils.createAuthorityList(member.getRole().getRole()));
    this.id = member.getMemberId();
  }

  public AuthUser(Claims claims) {
    super(
        claims.getSubject(),
        "",
        AuthorityUtils.commaSeparatedStringToAuthorityList(
            claims.get("authorities", String.class)));
    this.id = claims.get("id", Long.class);
  }
}
