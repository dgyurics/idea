package idea.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CaptchaRequest {
  @JsonProperty("response")
  private final String reCaptchaResponse;
  @JsonProperty("remoteip")
  private final String remoteAddr;
  @JsonProperty("secret")
  private final String secret;
}
