package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import idea.model.entity.Book;
import idea.model.request.BookRequestModel;
import idea.model.request.UserRequestModel;
import java.util.Collection;
import org.junit.After;
import org.junit.Test;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BookIT extends BaseIT {
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
  public void getBooks() {
    ResponseEntity<Collection<Book>> response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void uploadBook() {
    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).registrationCode(REGISTRATION_CODE).build();

    restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    final ResponseEntity<String> response = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    final String session = response.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", session);

    BookRequestModel book = BookRequestModel.builder()
        .author("author")
        .title("title")
        .alt("alt")
        .src("src").build();
    ResponseEntity<Book> responseCreateBook = restTemplate.exchange(getBookUri(), HttpMethod.PUT, new HttpEntity<>(book, headers), Book.class);
    assertEquals(HttpStatus.CREATED, responseCreateBook.getStatusCode());
    assertNotNull(responseCreateBook.getBody().getId());
  }
}
