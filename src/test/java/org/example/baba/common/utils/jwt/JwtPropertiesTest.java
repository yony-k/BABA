package org.example.baba.common.utils.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import org.example.baba.common.property.YamlPropertySourceFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(value = JwtProperties.class)
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
class JwtPropertiesTest {

  @Autowired
  @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
  private JwtProperties jwtProperties;

  @Test
  @DisplayName("yml 설정 파일 읽어 오기")
  void jwtPropertiesCheck() {
    // given
    // when
    // then
    assertAll(
        () -> assertThat(jwtProperties.getPrefix()).isEqualTo("Bearer "),
        () -> assertThat(jwtProperties.getAccessTokenValidityInSeconds()).isNotZero(),
        () -> assertThat(jwtProperties.getRefreshTokenValidityInSeconds()).isNotZero(),
        () -> assertThat(jwtProperties.getSecretKey()).isNotEmpty());
  }
}
