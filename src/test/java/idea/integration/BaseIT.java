package idea.integration;

import java.net.URI;
import java.util.Arrays;
import java.util.UUID;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.util.UriComponentsBuilder;

@RunWith(SpringRunner.class)
@TestPropertySource(locations = "/application-test.properties")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class BaseIT {
  @LocalServerPort int port;
  @Autowired TestRestTemplate restTemplate;

  URI getRegistrationUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/register").build().toUri();
  }

  URI getBookUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/book").build().toUri();
  }

  URI getBookUri(Long bookId) {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/book/{bookId}").buildAndExpand(bookId).toUri();
  }

  URI getDeleteAccountUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/unregister").build().toUri();
  }

  URI getTopicUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/topic").build().toUri();
  }

  URI getForgotPasswordUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/forgot-password").build().toUri();
  }

  URI getResetPasswordUri(String userId) {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/forgot-password/{userId}").buildAndExpand(userId).toUri();
  }

  URI getContactUsUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/contact").build().toUri();
  }

  URI getHealthCheckUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/health").build().toUri();
  }

  URI getLoginUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/login").build().toUri();
  }

  URI getRefreshUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/refresh").build().toUri();
  }

  URI getWhoAmIUri() {
    return UriComponentsBuilder.newInstance().scheme("http").host("localhost").port(port).path("/whoami").build().toUri();
  }
}