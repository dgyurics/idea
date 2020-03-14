package idea.config.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cookie.jwt")
@Data
public class CookieConfig {
  private String name;
  private String path;
  private String domain;
  private boolean httpOnly;
  private boolean secure;
  private int maxAgeInDays;
}
