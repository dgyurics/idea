package idea.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import idea.client.model.CaptchaRequest;
import idea.client.model.CaptchaResponse;

@Component
public class HttpClient {
  private final WebClient webClient;

  public HttpClient(@Value("${captcha.url}") String url) {
    webClient = WebClient.builder().baseUrl(url).build();
  }

  public CaptchaResponse validateCaptcha(CaptchaRequest request) {
    return webClient.post()
      .header("HttpHeaders.CONTENT_TYPE", MediaType.APPLICATION_JSON_VALUE)
      .contentType(MediaType.APPLICATION_JSON)
      .accept(MediaType.APPLICATION_JSON_UTF8)
      .syncBody(request)
      .retrieve()
      .bodyToMono(CaptchaResponse.class)
      .block();
  }
}
