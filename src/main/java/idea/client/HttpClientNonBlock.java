package idea.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import idea.client.model.CaptchaRequest;
import idea.client.model.CaptchaResponse;

@Component
public class HttpClientNonBlock {
  private final WebClient client;

  public HttpClientNonBlock(@Value("${captcha.url}") String url) {
    client = WebClient.builder().baseUrl(url).build();
  }

  public CaptchaResponse validateCaptcha(CaptchaRequest request) {
    return client.post()
      .uri(builder -> builder
           .queryParam("response", request.getReCaptchaResponse())
           .queryParam("secret", request.getSecret())
           .queryParam("remoteip", request.getRemoteAddr())
           .build())
      .header("HttpHeaders.CONTENT_TYPE", MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON)
      .retrieve()
      .bodyToMono(CaptchaResponse.class)
      .block();
  }
}
