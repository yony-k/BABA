package org.example.baba.repository;

import java.util.Optional;

import org.example.baba.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {
  Optional<Member> findByEmail(String email);

  boolean existsByMembername(String membername);

  boolean existsByEmail(String email);
}
