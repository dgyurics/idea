package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Collection;
import org.junit.After;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import idea.model.entity.Topic;
import idea.model.request.TopicRequestModel;
import idea.model.request.UserRequestModel;

public class TopicIT extends BaseIT {
  private static final String USERNAME = "username@gmail.com";
  private static final String PASSWORD = "password";
  private static final Integer REGISTRATION_CODE = 123456;

  @After
  public void after() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).build();
    restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
  }

  @Test
  public void createTopic__noSession() {
    TopicRequestModel requestBody = new TopicRequestModel();
    requestBody.setTitle("some title");
    ResponseEntity<Topic> response = restTemplate.exchange(getTopicUri(), HttpMethod.PUT, new HttpEntity<>(requestBody), Topic.class);
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void createTopic() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).registrationCode(REGISTRATION_CODE).build();

    restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    final ResponseEntity<String> response = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    final String session = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
    TopicRequestModel requestBody = new TopicRequestModel();
    requestBody.setTitle("some title");

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", session);

    ResponseEntity<Topic> responseCreateTopic = restTemplate.exchange(getTopicUri(), HttpMethod.PUT, new HttpEntity<>(requestBody, headers), Topic.class);
    assertNotNull(responseCreateTopic);
    assertEquals(HttpStatus.CREATED, responseCreateTopic.getStatusCode());
  }

  @Test
  public void getTopics() {
    ResponseEntity<Collection<Topic>> response = restTemplate.exchange(getTopicUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Topic>>(){});
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
