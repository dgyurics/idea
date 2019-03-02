package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import javax.ws.rs.core.HttpHeaders;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import idea.model.request.UserRequestModel;

public class AuthIT extends BaseIT {
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  @After
  public void after() {
    removeUser(USERNAME, PASSWORD);
  }

  @Test
  public void register__badRequest() {
    UserRequestModel model = UserRequestModel
        .builder().username("a").password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void register() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void register__userExists() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void register__duplicateEmails() {
    final String EMAIL = "email@gmail.com";

    /* Register new user */
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME).password(PASSWORD).email(EMAIL).build();
    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    /* Register new user */
    model = UserRequestModel
        .builder().username("randomuser").password(PASSWORD).email(EMAIL).build();
    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());    
  }

  @Test
  public void deleteUser() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void deleteUser__invalidCredentials() {
    UserRequestModel model = new UserRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    model.setPassword("password456");
    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void deleteUser__userNotExist() {
    UserRequestModel model = new UserRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    model.setUsername("johndoe");
    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void login__badCredentials() {
    ResponseEntity<String> response = restTemplate.exchange(getLoginUri("username", "password"), HttpMethod.POST, null, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getHeaders());
    assertNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
  }

  @Test
  public void login() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> responseRegister = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(responseRegister);
    assertEquals(HttpStatus.OK, responseRegister.getStatusCode());

    ResponseEntity<String> responseLogin = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    assertNotNull(responseLogin);
    assertEquals(HttpStatus.OK, responseLogin.getStatusCode());
    assertNotNull(responseLogin.getHeaders());
    assertNotNull(responseLogin.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
  }

  private void removeUser(String username, String password) {
    UserRequestModel model = UserRequestModel.builder()
        .username(username).password(password).build();
    restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
  }
}
