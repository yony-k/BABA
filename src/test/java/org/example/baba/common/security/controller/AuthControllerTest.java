package org.example.baba.common.security.controller;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

import java.util.Optional;

import jakarta.servlet.http.Cookie;

import org.example.baba.common.config.SecurityConfig;
import org.example.baba.common.config.dsl.JwtFilterDsl;
import org.example.baba.common.redis.RedisRepository;
import org.example.baba.common.security.config.SecurityTestConfig;
import org.example.baba.common.security.dto.LoginDto;
import org.example.baba.common.security.dto.MemberInfo;
import org.example.baba.common.utils.jwt.JwtProperties;
import org.example.baba.common.utils.jwt.JwtProvider;
import org.example.baba.domain.Member;
import org.example.baba.domain.enums.MemberRole;
import org.example.baba.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(AuthController.class)
@Import({SecurityTestConfig.class, SecurityConfig.class, JwtFilterDsl.class})
@MockBean(JpaMetamodelMappingContext.class)
class AuthControllerTest {

  @Autowired MockMvc mvc;
  @Autowired ObjectMapper objectMapper;
  @Autowired JwtProvider jwtProvider;
  @Autowired JwtProperties properties;
  @MockBean MemberRepository repository;
  @MockBean RedisRepository redis;

  LoginDto login;
  Member mockEntity;
  Member mockEntity2;
  MemberInfo memberInfo;

  @BeforeEach
  void init() {
    login = new LoginDto("kimminji@example.com", "password1");
    mockEntity =
        Member.builder()
            .memberId(1L)
            .email("kimminji@example.com")
            .memberName("김민지")
            .memberRole(MemberRole.USER)
            .password("{noop}password1")
            .build();
    mockEntity2 =
        Member.builder()
            .memberId(1L)
            .email("kimminji2@example.com")
            .memberName("김민지")
            .memberRole(MemberRole.USER)
            .password("{noop}password12")
            .build();
    memberInfo = new MemberInfo("kimminji@example.com", MemberRole.USER.getRole());
  }

  @Test
  @DisplayName("로그인 테스트 : 성공")
  void login_success_test() throws Exception {
    // given
    String content = objectMapper.writeValueAsString(login);

    given(repository.findByEmail(anyString())).willReturn(Optional.of(mockEntity));

    // when
    ResultActions perform =
        mvc.perform(
            MockMvcRequestBuilders.post("/api/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));
    // then
    perform
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
        .andExpect(MockMvcResultMatchers.cookie().exists("Refresh"));
  }

  @Test
  @DisplayName("로그인 테스트 : 실패")
  void login_failure_test() throws Exception {
    // given
    String content = objectMapper.writeValueAsString(login);

    given(repository.findByEmail(Mockito.anyString())).willReturn(Optional.of(mockEntity2));

    // when
    ResultActions perform =
        mvc.perform(
            MockMvcRequestBuilders.post("/api/login")
                .content(content)
                .contentType(MediaType.APPLICATION_JSON));
    // then
    perform.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @DisplayName("토큰 갱신 테스트 : 성공")
  void reissue_success_test() throws Exception {
    // given
    String mockString = objectMapper.writeValueAsString(memberInfo);

    given(repository.findByEmail(anyString())).willReturn(Optional.of(mockEntity));
    given(redis.findByKey(anyString())).willReturn(mockString);
    willDoNothing().given(redis).save(anyString(), anyString(), anyInt());
    // when
    ResultActions perform =
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue").cookie(createCookie()));
    // then
    perform
        .andExpect(MockMvcResultMatchers.status().isNoContent())
        .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.AUTHORIZATION))
        .andExpect(MockMvcResultMatchers.cookie().exists("Refresh"));
  }

  @Test
  @DisplayName("토큰 갱신 테스트 : 실패 [저장된 데이터가 없을 때]")
  void reissue_failure_data_not_exist_test() throws Exception {
    // given
    given(repository.findByEmail(anyString())).willReturn(Optional.of(mockEntity2));
    given(redis.findByKey(anyString())).willReturn(null);
    willDoNothing().given(redis).save(anyString(), anyString(), anyInt());
    // when
    ResultActions perform =
        mvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue").cookie(createCookie()));
    // then
    perform.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  @Test
  @DisplayName("토큰 갱신 테스트 : 실패 [쿠키가 없을 때]")
  void reissue_failure_cookie_not_exist_test() throws Exception {
    // given
    String mockString = objectMapper.writeValueAsString(memberInfo);

    given(repository.findByEmail(anyString())).willReturn(Optional.of(mockEntity));
    given(redis.findByKey(anyString())).willReturn(mockString);
    willDoNothing().given(redis).save(anyString(), anyString(), anyInt());
    // when
    ResultActions perform = mvc.perform(MockMvcRequestBuilders.post("/api/auth/reissue"));
    // then
    perform.andExpect(MockMvcResultMatchers.status().isUnauthorized());
  }

  private Cookie createCookie() {
    return new Cookie("Refresh", jwtProvider.generateRefreshToken(mockEntity.getEmail()));
  }
}
