package idea.integration;

import java.net.URI;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

// FIXME remove BaseIT and refactor to utility class
// Should not use inheritance for testing generally.
@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {
  @LocalServerPort int port;
  @Autowired TestRestTemplate restTemplate;

  protected URI getRegistrationUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/register").build().toUri();
  }

  protected URI getDeleteAccountUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/unregister").build().toUri();
  }

  protected URI getTopicUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/topic").build().toUri();
  }

  protected URI getForgotPasswordUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/forgot-password").build().toUri();
  }

  protected URI getResetPasswordUri(String userId) {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/forgot-password/{userId}").buildAndExpand(userId).toUri();
  }

  protected URI getContactUsUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/contact").build().toUri();
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
}
