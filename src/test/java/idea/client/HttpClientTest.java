package idea.client;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.matching;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.postRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.apache.commons.lang3.StringUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import idea.client.model.CaptchaRequest;
import idea.client.model.CaptchaResponse;

@RunWith(SpringRunner.class)
public class HttpClientTest {
  @Rule
  public WireMockRule wireMockRule = new WireMockRule(8088);
  private static final String BASE_URL = "localhost:8088";
  private static final String URL = "/recaptcha/api/siteverify";

  @Test
  public void validCaptcha() throws InterruptedException {
    final HttpClientNonBlock client = new HttpClientNonBlock(BASE_URL+URL);
    final CaptchaRequest request = new CaptchaRequest("captcharesponse", "remoteAdr", "secret");

    stubFor(post(urlEqualTo(URL))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .withBodyFile("wiremock/captchaSuccess.json")));

    final CaptchaResponse response = client.validateCaptchaNonBlock(request).block();
    assertTrue(response.isSuccess());
    assertTrue(StringUtils.isNotBlank(response.getChallengeTs()));
    assertTrue(StringUtils.isNotBlank(response.getHostname()));
    assertNotNull(response.getErrors());
    assertTrue(response.getErrors().length == 0);

    verify(postRequestedFor(urlMatching(URL))
        .withHeader("Content-Type", matching(MediaType.APPLICATION_JSON_VALUE)));
  }
}
