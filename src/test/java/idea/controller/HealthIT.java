package idea.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthIT extends BaseIT {

  @Before
  public void init() {
    super.init();
  }

  @Test
  public void healthCheck() {
    ResponseEntity<Object> response = restTemplate.exchange(getHealthCheckUri(),HttpMethod.GET, null, Object.class);
    assertNotNull(response);
    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
  }
}
