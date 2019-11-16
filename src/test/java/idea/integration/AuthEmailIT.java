package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.ServerSetupTest;
import idea.model.request.UserRequestModel;

public class AuthEmailIT extends BaseIT {
  private static final String USERNAME = "myemail@yahoo.com";
  private static final String PASSWORD = "password";
  private static final Integer REGISTRATION_CODE = 123456;

  @ClassRule
  public static final GreenMailRule greenMail = new GreenMailRule(ServerSetupTest.SMTP);

  @BeforeClass
  public static void before() {
    greenMail.setUser("username", "password123"); //credentials stored in test configuration file
  }

  @Before
  public void beforeEach() {
    removeUser(USERNAME, PASSWORD);
    registerUser(USERNAME, PASSWORD, REGISTRATION_CODE);
  }

  @After
  public void afterEach() {
    greenMail.reset();
  }

  // FIXME refactor
  @Test
  public void requestPasswordChange() throws MessagingException, IOException {
    requestPasswordReset(USERNAME, HttpStatus.OK);
    assertTrue(greenMail.waitForIncomingEmail(5000, 1));

    final MimeMessage email = greenMail.getReceivedMessages()[0];
    final String subject = email.getSubject();
    final String content = String.valueOf(email.getContent());
    assertEquals(subject, "Password reset");
    assertTrue(content.contains("Your reset code is "));

    // extract reset code
    final String resetCode = getString(content, Pattern.compile("[0-9]{6}"), 0);
    final String userId = getString(content, Pattern.compile("(\\/)([0-9]{1,10})"), 2);

    // reset password
    passwordReset(resetCode, userId, "newpassword123", HttpStatus.OK);
    assertEquals(401, login(USERNAME, PASSWORD).getStatusCodeValue());
    assertEquals(200, login(USERNAME, "newpassword123").getStatusCodeValue());

    // try to use reset code again
    passwordReset(resetCode, userId, "newpassword1234", HttpStatus.BAD_REQUEST);
    assertEquals(401, login(USERNAME, PASSWORD).getStatusCodeValue());
    assertEquals(200, login(USERNAME, "newpassword123").getStatusCodeValue());

    // TODO try to use reset code after 15 minutes has elapsed (reset code should expire by then)
  }

  private String getString(String content, Pattern pattern, int group) {
    final Matcher matcher = pattern.matcher(content);
    assertTrue(matcher.find());
    return matcher.group(group);
  }

  private ResponseEntity<String> login(String username, String password) {
     return restTemplate.exchange(getLoginUri(username, password), HttpMethod.POST, null, String.class);
  }
  private void passwordReset(String resetCode, String userId, String newPassword, HttpStatus expectedHttpStatus) {
    final UserRequestModel model = UserRequestModel.builder()
        .password(newPassword)
        .resetCode(Integer.parseInt(resetCode))
        .build();
    ResponseEntity<String> response = restTemplate.exchange(getResetPasswordUri(userId), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(expectedHttpStatus, response.getStatusCode());
  }

  private void requestPasswordReset(String username, HttpStatus expectedHttpStatus) {
    final UserRequestModel model = UserRequestModel.builder()
        .username(username).build();
    ResponseEntity<String> response = restTemplate.exchange(getForgotPasswordUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(expectedHttpStatus, response.getStatusCode());
  }

  private void registerUser(String username, String password, int registrationCode) {
    final UserRequestModel model = UserRequestModel.builder()
        .username(username)
        .password(password)
        .registrationCode(registrationCode)
        .build();
    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  private void removeUser(String username, String password) {
    UserRequestModel model = UserRequestModel.builder()
        .username(username).password(password).build();
    restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
  }
}
