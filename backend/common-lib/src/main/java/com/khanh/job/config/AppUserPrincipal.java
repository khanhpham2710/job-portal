package com.khanh.job.config;

import java.security.Principal;
import java.util.UUID;
import org.jspecify.annotations.Nullable;

public record AppUserPrincipal(UUID userId, @Nullable String email) implements Principal {

  @Override
  public String getName() {
    return userId.toString();
  }
}
