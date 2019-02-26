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
import idea.model.request.RegistrationRequestModel;

public class AuthIT extends BaseIT {
  private static final String USERNAME = "username";
  private static final String PASSWORD = "password";

  @After
  public void after() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);  
    restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
  }

  @Test
  public void register__badRequest() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername("");
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void register() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void register__userExists() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());    
  }

  @Test
  public void deleteUser() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void deleteUser__invalidCredentials() {
    RegistrationRequestModel model = new RegistrationRequestModel();
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
    RegistrationRequestModel model = new RegistrationRequestModel();
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
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> responseRegister = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(responseRegister);
    assertEquals(HttpStatus.OK, responseRegister.getStatusCode());

    ResponseEntity<String> responseLogin = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    assertNotNull(responseLogin);
    assertEquals(HttpStatus.OK, responseLogin.getStatusCode());
    assertNotNull(responseLogin.getHeaders());
    assertNotNull(responseLogin.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
  }
}
