package org.example.baba.domain;

import java.io.Serializable;

import jakarta.persistence.Id;

import org.springframework.data.redis.core.RedisHash;

import lombok.*;

@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@RedisHash(value = "ApprovalCode", timeToLive = 600)
public class ApprovalCode implements Serializable {

  @Id String approvalKey;

  String email;
}
