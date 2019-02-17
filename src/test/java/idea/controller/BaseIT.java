package idea.controller;

import java.net.URI;
import javax.ws.rs.core.HttpHeaders;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;
import idea.model.request.RegistrationRequestModel;

// FIXME remove BaseIT and refactor to utility class
// Should not use inheritance for testing generally.
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {
  private static final String USERNAME = "dennis";
  private static final String PASSWORD = "dennis";
  private static String session = null;

  @LocalServerPort int port;
  @Autowired TestRestTemplate restTemplate;

  protected void init() {
    RegistrationRequestModel model = new RegistrationRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);
    restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
  }

  protected URI getRegistrationUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/register").build().toUri();
  }

  protected URI getHealthCheckUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/health").build().toUri();
  }

  protected URI getLoginUri(String username, String password) {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/login")
        .query("username={username}")
        .query("password={password}")
        .buildAndExpand(username, password)
        .toUri();
  }

  // FIXME: super.init() must be called for this to work.
  // annotating BaseIT.init with @Before does not seem to work.
  protected String getSession() {
    if(session == null) {
      ResponseEntity<String> response =
          restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
      session = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    }
    return session;
  }
}
