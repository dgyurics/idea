package idea.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
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
import idea.model.request.ContactUsRequestModel;

public class ContactUsIT extends BaseIT {
  private static final String URL_CAPTCHA = "/recaptcha/api/siteverify";
  @Rule
  public WireMockRule instanceRule = new WireMockRule(8088);
  @ClassRule
  public static final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

  @BeforeClass
  public static void before() {
    greenMail.setUser("username", "password123"); //credentials stored in test configuration file
  }

  @After
  public void afterEach() throws FolderException {
    greenMail.purgeEmailFromAllMailboxes();
  }

  @Test
  public void submitContactUsMessage() throws Exception {
    mockCaptchaEndpoint("wiremock/captchaSuccess.json");
    final String phoneNumber = "1233332222";
    final String message = "hello world";
    final String reCaptchaResponse = "reCaptchaResponse";
    final ContactUsRequestModel model = createRequest(phoneNumber, message);
    final ResponseEntity<String> response = createRequest(model, reCaptchaResponse);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    final MimeMessage email = greenMail.getReceivedMessages()[0];
    final String subject = email.getSubject();
    final String content = String.valueOf(email.getContent());
    assertEquals(subject, "lagom.life message");
    assertTrue(content.contains(phoneNumber));    
  }

  @Test
  public void submitContactUsMessage_BadCaptcha() throws Exception {
    mockCaptchaEndpoint("wiremock/captchaFail.json");
    final String phoneNumber = "1233332222";
    final String message = "hello world";
    final String reCaptchaResponse = "reCaptchaResponse";
    final ContactUsRequestModel model = createRequest(phoneNumber, message);
    final ResponseEntity<String> response = createRequest(model, reCaptchaResponse);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    final MimeMessage[] email = greenMail.getReceivedMessages();
    assertTrue(email.length == 0);
  }

  private ContactUsRequestModel createRequest(String contactInfo, String message) {
    return ContactUsRequestModel.builder()
      .contactInfo(contactInfo)
      .message(message).build();
  }

  private ResponseEntity<String> createRequest(ContactUsRequestModel model, String reCaptchaResponse) {
    return restTemplate.exchange(getContactUsUri(reCaptchaResponse), HttpMethod.POST, new HttpEntity<>(model), String.class);
  }

  private static void mockCaptchaEndpoint(String bodyFile) {
    stubFor(post(urlEqualTo(URL_CAPTCHA))
        .willReturn(aResponse()
            .withStatus(200)
            .withHeader("Content-Type", MediaType.APPLICATION_JSON_UTF8_VALUE)
            .withBodyFile(bodyFile)));
  }
}
