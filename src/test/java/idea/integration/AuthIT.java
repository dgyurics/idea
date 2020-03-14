package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import idea.model.dto.UserRequestModel;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.time.Clock;
import java.util.Date;
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

    /* verify refresh token received, and then some */
    verifyRefreshToken(response);

    /* verify JWT token received, and then some */
    verifyJwtToken(response, USERNAME, false);
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

    /* verify refresh token received, and then some */
    verifyRefreshToken(response);

    /* verify JWT token received, and then some */
    verifyJwtToken(response, USERNAME_ADMIN, true);
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
    ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());

    // create refresh token
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response.getBody());
    // the controller reads this and sees multiple cookies
    headers.add("Cookie", verifyRefreshToken(response));

    // validate current JWT token works
    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    // try to use expired JWT token (expires after 5 seconds according to config file)
    Thread.sleep(5000); // FIXME
    response = restTemplate.exchange(getWhoAmIUri(),HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());

    // obtain new JWT token using refresh cookie/token
    response = restTemplate.exchange(getRefreshUri(), HttpMethod.GET, new HttpEntity<>(headers), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.CREATED, response.getStatusCode());
    assertNotNull(response.getBody());

    // try to use new JWT token
    headers.set("Authorization", "Bearer " + response.getBody());
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

  private void verifyJwtToken(ResponseEntity<String> res,
      String username,
      boolean isAdmin) {
    final Claims claims = decodeJWT(res.getBody());
    assertEquals(DOMAIN, claims.getIssuer());
    assertEquals(isAdmin, (Boolean) claims.get("admin"));
    assertNotNull(claims.get("id"));
    assertEquals(username, claims.get("username"));
    assertTrue(new Date().after(claims.getIssuedAt()));
    assertTrue(new Date().before(claims.getExpiration()));
  }

  private String verifyRefreshToken(ResponseEntity res) {
    // respoonse.headers.['Set-Cookie'] (list)
    // user=6946d0d0-8f52-4d3c-9e71-d58ebec67931;
    // Max-Age=2678400; Expires=Wed, 01-Apr-2020 20:10:31 GMT;
    // Domain=lagom.life; Path=/
    assertTrue(res.getHeaders().containsKey("Set-Cookie"));
    // this returns a string
    assertNotNull(res.getHeaders().getFirst(HttpHeaders.SET_COOKIE));
    return res.getHeaders().getFirst(HttpHeaders.SET_COOKIE);
  }
}
