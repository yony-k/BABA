package org.example.baba.domain;

import jakarta.persistence.*;

import org.example.baba.domain.enums.MemberRole;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long memberId;

  @Column(unique = true, nullable = false)
  private String memberName;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private MemberRole role;

  @Builder
  public Member(
      Long memberId, String memberName, String email, String password, MemberRole memberRole) {
    this.memberId = memberId;
    this.memberName = memberName;
    this.email = email;
    this.password = password;
    this.role = memberRole;
  }
}
