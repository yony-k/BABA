package org.example.baba.common.annotation;

import java.util.Arrays;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithUser> {

  @Override
  public SecurityContext createSecurityContext(WithUser annotation) {
    final SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
    final UsernamePasswordAuthenticationToken authenticationToken =
        new UsernamePasswordAuthenticationToken(
            annotation.memberId(),
            "password",
            Arrays.asList(new SimpleGrantedAuthority(annotation.userRole().getRole())));

    securityContext.setAuthentication(authenticationToken);
    return securityContext;
  }
}
