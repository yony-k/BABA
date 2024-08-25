package org.example.baba.common.config;

import java.util.Arrays;

import org.example.baba.common.config.dsl.JwtFilterDsl;
import org.example.baba.common.security.handler.AuthenticationEntryPointHandler;
import org.example.baba.common.security.handler.LogoutSuccessCustomHandler;
import org.example.baba.common.security.handler.VerificationAccessDeniedHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtFilterDsl jwtFilterDsl;
  private final AuthenticationEntryPointHandler authenticationEntryPointHandler;
  private final VerificationAccessDeniedHandler verificationAccessDeniedHandler;
  private final LogoutSuccessCustomHandler logoutSuccessCustomHandler;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    http.with(jwtFilterDsl, JwtFilterDsl::build);
    http.headers(
        headerConfig -> headerConfig.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
    http.authorizeHttpRequests(
            authorize ->
                authorize
                    .requestMatchers("/api/admin/**")
                    .hasRole("ADMIN")
                    .requestMatchers("/api/stats/**")
                    .hasAnyRole("USER", "ADMIN")
                    .anyRequest()
                    .permitAll())
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        // cors 관련 옵션 끄기
        .cors(cors -> cors.configurationSource(apiConfigurationSource()))
        .csrf(AbstractHttpConfigurer::disable);
    http.exceptionHandling(
        exception ->
            exception
                .authenticationEntryPoint(authenticationEntryPointHandler)
                .accessDeniedHandler(verificationAccessDeniedHandler));
    http.logout(
        logout -> logout.logoutSuccessHandler(logoutSuccessCustomHandler).logoutUrl("/api/logout"));
    return http.build();
  }

  private CorsConfigurationSource apiConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOriginPatterns(Arrays.asList("*"));
    configuration.setAllowedMethods(
        Arrays.asList("GET", "POST", "PATCH", "DELETE", "OPTIONAL", "OPTIONS"));
    configuration.setAllowCredentials(true);
    configuration.setAllowedHeaders(Arrays.asList("*"));
    configuration.setExposedHeaders(
        Arrays.asList("Authorization", "Location", "Refresh", "authorization"));
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);

    return source;
  }
}
