package com.khanh.job.config;

import jakarta.persistence.EntityManager;
import java.util.Optional;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

@AutoConfiguration
@ConditionalOnClass({EntityManager.class, EnableJpaAuditing.class})
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
public class AuditingConfiguration {

  @Bean(name = "auditorAware")
  @ConditionalOnMissingBean(name = "auditorAware")
  AuditorAware<String> auditorAware() {
    return () -> {
      var auth = SecurityContextHolder.getContext().getAuthentication();
      if (auth == null
          || !auth.isAuthenticated()
          || auth instanceof AnonymousAuthenticationToken
          || !(auth.getPrincipal() instanceof AppUserPrincipal principal)) {
        return Optional.of("system");
      }
      return Optional.of(principal.getName());
    };
  }
}
