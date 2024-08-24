package org.example.baba.domain;

import java.io.Serializable;

import org.example.baba.domain.enums.MemberRole;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@RedisHash(value = "Register", timeToLive = 7200)
@ToString
public class Register implements Serializable {

  @Id String email;

  String memberName;
  String password;

  public Member toMember(int subStringIndex) {
    return Member.builder()
        .email(this.email.substring(subStringIndex))
        .memberName(this.memberName)
        .password(this.password)
        .memberRole(MemberRole.USER)
        .build();
  }
}
