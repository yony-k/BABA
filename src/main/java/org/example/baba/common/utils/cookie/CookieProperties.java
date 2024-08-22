package org.example.baba.common.utils.cookie;

import org.example.baba.common.property.YamlPropertySourceFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Component
@ConfigurationProperties("cookie")
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
public class CookieProperties {

  private int limitTime;
  private String acceptedUrl;
  private String cookieName;
  private String domain;
}
