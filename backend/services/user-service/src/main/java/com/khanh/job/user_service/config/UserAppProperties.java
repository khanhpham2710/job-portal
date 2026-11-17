package com.khanh.job.user_service.config;

import com.khanh.job.config.CommonAppProperties;
import java.time.Duration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "application")
public class UserAppProperties extends CommonAppProperties {

  private Refresh refresh = new Refresh();

  @Getter
  @Setter
  public static class Refresh {
    private CommonAppProperties.Token token = defaultToken();

    private static CommonAppProperties.Token defaultToken() {
      CommonAppProperties.Token t = new CommonAppProperties.Token();
      t.setExpiration(Duration.ofDays(7));
      System.out.println("sadsa");
      return t;
    }
  }
}
