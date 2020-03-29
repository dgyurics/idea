package idea.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlMatching;
import static org.awaitility.Awaitility.await;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.util.concurrent.TimeUnit;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.store.FolderException;
import com.icegreen.greenmail.util.ServerSetupTest;
import idea.model.dto.ContactUsRequestModel;

/**
 * Validates the correct HTTP requests are made when a new contact us message
 * is received.
 * Note: Mock email server and Mock Captcha endpoint information exists in
 * test configuration file {@code application-test.properties}
 */
public class ContactUsIT extends BaseIT {
  @Rule
  public WireMockRule captchaMockEndpoint = new WireMockRule(8088);
  @ClassRule
  public static final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

  @BeforeClass
  public static void before() {
    greenMail.setUser("username", "password123");
  }

  @After
  public void afterEach() throws FolderException {
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  public void submitContactUsMessage() throws Exception {
    mockCaptchaEndpoint("/recaptcha/api/siteverify.*");
    final String phoneNumber = "1233332222";
    final String message = "hello world";
    final String reCaptchaResponse = "reCaptchaResponse";
    final ContactUsRequestModel model = createRequest(phoneNumber, message, reCaptchaResponse);
    final ResponseEntity<String> response = createRequest(model);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    await().atMost(10, TimeUnit.SECONDS).until(() -> greenMail.getReceivedMessages().length > 0);

    final MimeMessage email = greenMail.getReceivedMessages()[0];
    final String subject = email.getSubject();
    final String content = String.valueOf(email.getContent());
    assertEquals(subject, "lagom.life message");
    assertTrue(content.contains(phoneNumber));    
  }

  private ContactUsRequestModel createRequest(String contactInfo, String message, String reCaptchaResponse) {
    return ContactUsRequestModel.builder()
      .contactInfo(contactInfo)
      .message(message)
      .reCaptchaResponse(reCaptchaResponse).build();
  }

  private ResponseEntity<String> createRequest(ContactUsRequestModel model) {
    return restTemplate.exchange(getContactUsUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
  }

  private static void mockCaptchaEndpoint(String urlRegex) {
    stubFor(post(urlMatching(urlRegex))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .withBodyFile("wiremock/captchaSuccess.json")));
  }
}
