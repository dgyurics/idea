package idea.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import java.net.URI;
import javax.ws.rs.core.HttpHeaders;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import idea.model.entity.User;
import idea.model.request.RegistrationRequestModel;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class AuthIT {

  @LocalServerPort
  private int port;

  @Autowired
  private TestRestTemplate restTemplate;

  @Test
  public void register__badRequest() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername("");
    model.setPassword("password");

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void register() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername("denxnis");
    model.setPassword("password");

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void register__userExists() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername("denxnis2");
    model.setPassword("password");

    HttpEntity<RegistrationRequestModel> request = new HttpEntity<>(model);
    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, request, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, request, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());    
  }

  @Test
  public void login__badCredentials() {
    User user = new User();
    user.setUsername("username");
    user.setPassword("password");

    ResponseEntity<String> response = restTemplate.exchange(getLoginUri("username", "password"), HttpMethod.POST, new HttpEntity<>(user), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNotNull(response.getHeaders());
    assertNull(response.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
  }

  @Test
  public void login() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername("denxnis3");
    model.setPassword("password");

    ResponseEntity<String> responseRegister = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(responseRegister);
    assertEquals(HttpStatus.OK, responseRegister.getStatusCode());

    ResponseEntity<String> responseLogin = restTemplate.exchange(getLoginUri("denxnis3", "password"), HttpMethod.POST, null, String.class);
    assertNotNull(responseLogin);
    assertEquals(HttpStatus.OK, responseLogin.getStatusCode());
    assertNotNull(responseLogin.getHeaders());
    assertNotNull(responseLogin.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
  }

  private URI getRegistrationUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/register").build().toUri();    
  }

  private URI getLoginUri(String username, String password) {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/login")
        .query("username={username}")
        .query("password={password}")
        .buildAndExpand(username, password)
        .toUri();
  }
}
