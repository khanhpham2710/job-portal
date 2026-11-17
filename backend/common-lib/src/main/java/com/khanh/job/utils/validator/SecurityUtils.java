package com.khanh.job.utils.validator;

import com.khanh.job.config.AppUserPrincipal;
import java.util.Optional;
import java.util.UUID;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public final class SecurityUtils {

  private SecurityUtils() {}

  public static Optional<AppUserPrincipal> currentPrincipal() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
    if (auth != null && auth.getPrincipal() instanceof AppUserPrincipal p) {
      return Optional.of(p);
    }
    return Optional.empty();
  }

  public static Optional<UUID> currentUserId() {
    return currentPrincipal().map(AppUserPrincipal::userId);
  }
}
