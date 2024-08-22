package org.example.baba.domain;

import jakarta.persistence.*;

import org.example.baba.domain.enums.UserRole;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long userId;

  @Column(unique = true, nullable = false)
  private String username;

  @Column(unique = true, nullable = false)
  private String email;

  @Column(nullable = false)
  private String password;

  @Enumerated(EnumType.STRING)
  private UserRole role;

  @Builder
  public User(Long userId, String username, String email, String password, UserRole userRole) {
    this.userId = userId;
    this.username = username;
    this.email = email;
    this.password = password;
    this.role = userRole;
  }
}
