package org.example.baba.common.security.details;

import org.example.baba.domain.Member;
import org.example.baba.exception.CustomException;
import org.example.baba.exception.exceptionType.AuthorizedExceptionType;
import org.example.baba.repository.MemberRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberDetailService implements UserDetailsService {

  private final MemberRepository repository;

  @Override
  public UserDetails loadUserByUsername(String email) {

    Member member =
        repository
            .findByEmail(email)
            .orElseThrow(() -> new CustomException(AuthorizedExceptionType.UNAUTHENTICATED));

    return new AuthUser(member);
  }
}
