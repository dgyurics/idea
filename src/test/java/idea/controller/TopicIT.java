package idea.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.net.URI;
import java.util.Collection;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import idea.model.entity.Topic;
import idea.model.request.TopicRequestModel;

public class TopicIT extends BaseIT {
  private URI topicUri;

  @Before
  public void init() {
    super.init();
    topicUri = UriComponentsBuilder.newInstance()
        .scheme("http").host("localhost").port(port).path("/topic").build().toUri();
  }

  @Test
  public void createTopic__noSession() {
    TopicRequestModel requestBody = new TopicRequestModel();
    requestBody.setTitle("some title");
    ResponseEntity<Topic> response = restTemplate.exchange(topicUri, HttpMethod.PUT, new HttpEntity<>(requestBody), Topic.class);
    assertNotNull(response);
    assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
  }

  @Test
  public void createTopic() {
    TopicRequestModel requestBody = new TopicRequestModel();
    requestBody.setTitle("some title");
    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", getSession());
    ResponseEntity<Topic> response = restTemplate.exchange(topicUri, HttpMethod.PUT, new HttpEntity<>(requestBody, headers), Topic.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
  }

  @Ignore
  public void deleteTopic_noSession() {
    // TODO
  }

  @Test
  public void getTopics() {
    ResponseEntity<Collection<Topic>> response = restTemplate.exchange(topicUri, HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Topic>>(){});
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }
}
