package org.example.baba.common.annotation;

import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.example.baba.domain.enums.MemberRole;
import org.springframework.security.test.context.support.WithSecurityContext;

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithUser {
  long memberId() default 2L;

  MemberRole userRole() default MemberRole.USER;
}
