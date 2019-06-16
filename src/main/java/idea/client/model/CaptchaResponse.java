package idea.client.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CaptchaResponse {
  @JsonProperty("success")
  private boolean success;
  @JsonProperty("challenge_ts")
  private String challengeTs;
  @JsonProperty("hostname")
  private String hostname;
  @JsonProperty("error-codes")
  private String[] errors;
}
