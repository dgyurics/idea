package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import idea.model.dto.UserRequestModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.util.Date;
import java.util.UUID;
import javax.xml.bind.DatatypeConverter;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class AuthIT extends BaseIT {
  private static final String DOMAIN = "lagom.life";
  private static final String USERNAME_ADMIN = "lagom3922@gmail.com";
  private static final String PASSWORD_ADMIN = "password123";
  private static final String USERNAME = "username@gmail.com";
  private static final String PASSWORD = "password";

  @Value("${jwt.secret}")
  String SECRET_KEY;

  @SpyBean
  Clock clock;

  @Autowired
  ApplicationContext context;

  @Before
  public void before() throws Exception {
    CommandLineRunner runner = context.getBean(CommandLineRunner.class);
    runner.run( USERNAME_ADMIN, PASSWORD_ADMIN);
  }

  @After
  public void after() {
    removeUser(USERNAME, PASSWORD);
    removeUser(USERNAME_ADMIN, PASSWORD_ADMIN);
  }

  @Test
  public void register() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME).password(PASSWORD).build();

    final ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());

    /* validate JWT token */
    final Claims claims = decodeJWT(response.getBody());
    assertEquals(DOMAIN, claims.getIssuer());
    assertFalse((Boolean) claims.get("admin"));
    assertNotNull(claims.get("id"));
    assertEquals(USERNAME, claims.get("username"));
    assertTrue(new Date().after(claims.getIssuedAt()));
    assertTrue(new Date().before(claims.getExpiration()));
  }

  @Test
  public void register_userExists() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
  }

  @Test
  public void login() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    final ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    /* validate JWT token */
    final Claims claims = decodeJWT(response.getBody());
    assertEquals(DOMAIN, claims.getIssuer());
    assertTrue((Boolean) claims.get("admin"));
    assertNotNull(claims.get("id"));
    assertEquals(USERNAME_ADMIN, claims.get("username"));
    assertTrue(new Date().after(claims.getIssuedAt()));
    assertTrue(new Date().before(claims.getExpiration()));
  }

  @Test
  public void validToken() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    /* try to access secure page */
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response.getBody());

    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void expiredToken() {
    /* Create JWT uses Java.time.Clock when setting the expiration time, "ex".
     By moving the time back 20 minutes, we are creating an expired token.
     After creating the expired token, we attempt to access a secure endpoint,
     which results in 403 because our token has expired.
     Note the library used to decrypt the token does not use Java.time.Clock and is un-effected by
     our spybean */
    when(clock.millis()).thenReturn(DateUtils.addMinutes(new Date(), -20).toInstant().toEpochMilli());

    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    /* try to access secure page */
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response.getBody());

    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
  }

  @Test
  public void login__badCredentials() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password("abadpassword").build();
    final ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());
  }

  @Test
  public void deleteUser() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).build();

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    assertNull(response.getBody());

    // TODO validate refresh token has been removed/deleted
  }

  @Test
  public void deleteUser__invalidCredentials() {
    UserRequestModel model = new UserRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());

    model.setPassword("password456");
    response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
  }

  @Test
  public void deleteUser__userNotExist() {
    UserRequestModel model = new UserRequestModel();
    model.setUsername(USERNAME);
    model.setPassword(PASSWORD);

    ResponseEntity<String> response = restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

  }

  @Test
  public void refreshToken() throws InterruptedException {
    // login to get new JWT token
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    ResponseEntity<String> response_JwtToken = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response_JwtToken);
    assertEquals(HttpStatus.OK, response_JwtToken.getStatusCode());
    assertNotNull(response_JwtToken.getBody());

    // create refresh token
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response_JwtToken.getBody());

    ResponseEntity<UUID> response_RefreshToken = restTemplate.exchange(getRefreshUri(), HttpMethod.GET, new HttpEntity<>(headers), UUID.class);
    assertNotNull(response_RefreshToken);
    assertEquals(HttpStatus.CREATED, response_RefreshToken.getStatusCode());
    assertNotNull(response_RefreshToken.getBody());

    // validate current JWT token works
    ResponseEntity<String> response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // try to use expired JWT token (expires after 5 seconds according to config file)
    Thread.sleep(5000); // FIXME
    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    // obtain new JWT token using refresh token
    final UUID refreshToken = response_RefreshToken.getBody();
    response_JwtToken = restTemplate.exchange(getRefreshJwtUri(refreshToken), HttpMethod.GET, null, String.class);
    assertNotNull(response_JwtToken);
    assertEquals(HttpStatus.OK, response_JwtToken.getStatusCode());
    assertNotNull(response_JwtToken.getBody());

    // try to use new JWT token
    headers.set("Authorization", "Bearer " + response_JwtToken.getBody());
    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  private Claims decodeJWT(String jwt) {
    return Jwts.parser()
        .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
        .parseClaimsJws(jwt).getBody();
  }

  private void removeUser(String username, String password) {
    UserRequestModel model = UserRequestModel.builder()
        .username(username).password(password).build();
    restTemplate.exchange(getRegistrationUri(), HttpMethod.DELETE, new HttpEntity<>(model), String.class);
  }
}
