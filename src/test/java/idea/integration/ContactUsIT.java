package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import idea.model.request.ContactUsRequestModel;
import idea.model.request.UserRequestModel;

public class ContactUsIT extends BaseIT {

  @ClassRule
  public static final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

  @BeforeClass
  public static void before() {
    greenMail.setUser("username", "password123"); //credentials stored in test configuration file
  }

  @After
  public void afterEach() {
    greenMail.reset();
  }

  @Test
  public void submitContactUsMessage() throws MessagingException, IOException {
    final String phoneNumber = "1233332222";
    final String message = "hello world";
    final ContactUsRequestModel model = createRequest(phoneNumber, message);
    ResponseEntity<String> response = createRequest(model);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    final MimeMessage email = greenMail.getReceivedMessages()[0];
    final String subject = email.getSubject();
    final String content = String.valueOf(email.getContent());
    assertEquals(subject, "lagom.life message");
    assertTrue(content.contains(phoneNumber));    
  }

  private ContactUsRequestModel createRequest(String contactInfo, String message) {
    return ContactUsRequestModel.builder()
      .contactInfo(contactInfo)
      .message(message).build();
  }

  private ResponseEntity<String> createRequest(ContactUsRequestModel model) {
    return restTemplate.exchange(getContactUsUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
  }
}
