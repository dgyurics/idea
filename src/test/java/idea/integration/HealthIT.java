package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HealthIT extends BaseIT {
  @Test
  public void healthCheck() {
    ResponseEntity<String> response = restTemplate.exchange(getHealthCheckUri(),HttpMethod.GET, null, String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.ACCEPTED, response.getStatusCode());
  }
}
