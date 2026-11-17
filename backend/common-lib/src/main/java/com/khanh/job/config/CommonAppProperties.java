package com.khanh.job.config;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class CommonAppProperties {

  private Security security = new Security();

  @Getter
  @Setter
  public static class Security {
    private Jwt jwt = new Jwt();
  }

  @Getter
  @Setter
  public static class Jwt {
    private Token access = new Token();
    private List<String> publicPaths = new ArrayList<>(List.of("/actuator/health", "/api/auth/**"));
  }

  @Getter
  @Setter
  public static class Token {
    private String secret = "";

    private String issuer = "job-portal";

    @Setter private Duration expiration = Duration.ofMinutes(30);
  }
}
