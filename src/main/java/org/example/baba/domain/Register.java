package org.example.baba.domain;

import java.io.Serializable;

import jakarta.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@RedisHash(value = "Register", timeToLive = 7200)
public class Register implements Serializable {

  @Id String email;

  String memberName;
  String password;

  public Member toMember() {
    return Member.builder()
        .email(this.email)
        .memberName(this.memberName)
        .password(this.password)
        .build();
  }
}
