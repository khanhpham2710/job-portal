package com.khanh.job.config;

import com.khanh.job.security.JwtAuthenticationFilter;
import com.khanh.job.security.JwtAuthenticator;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@AutoConfiguration
@ConditionalOnWebApplication(type = ConditionalOnWebApplication.Type.SERVLET)
@ConditionalOnProperty(prefix = "application.security.jwt", name = "secret-key")
@EnableMethodSecurity
public class ApplicationConfiguration {

  /** Bean mặc định, tự lùi lại nếu service có class con của CommonAppProperties. */
  @Bean
  @ConditionalOnMissingBean(CommonAppProperties.class)
  CommonAppProperties commonAppProperties() {
    return new CommonAppProperties();
  }

  @Bean
  @ConditionalOnMissingBean
  JwtAuthenticator jwtAuthenticator(CommonAppProperties props) {
    return new JwtAuthenticator(props.getSecurity().getJwt());
  }

  @Bean
  @ConditionalOnMissingBean(SecurityFilterChain.class)
  SecurityFilterChain securityFilterChain(
      HttpSecurity http, JwtAuthenticator jwtAuthenticator, CommonAppProperties props)
      throws Exception {

    String[] publicPaths = props.getSecurity().getJwt().getPublicPaths().toArray(String[]::new);

    http.csrf(AbstractHttpConfigurer::disable)
        .sessionManagement(s -> s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth -> auth.requestMatchers(publicPaths).permitAll().anyRequest().authenticated())
        .exceptionHandling(
            e -> e.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)))
        .addFilterBefore(
            new JwtAuthenticationFilter(jwtAuthenticator),
            UsernamePasswordAuthenticationFilter.class);

    return http.build();
  }
}
