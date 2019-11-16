package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import idea.model.entity.Book;
import idea.model.request.BookRequestModel;
import idea.model.request.UserRequestModel;
import idea.service.BookService;
import java.util.Collection;
import org.junit.After;
import org.junit.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.SpyBean;
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

  @SpyBean
  BookService bookService;

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

    ResponseEntity<Collection<Book>> responseBooks = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, responseBooks.getStatusCode());
    assertNotNull(responseBooks.getBody());
    assertEquals(6, responseBooks.getBody().size());
  }

  @Test
  public void removeBook() throws InterruptedException {
    ResponseEntity<Collection<Book>> response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() >= 5);

    UserRequestModel model = UserRequestModel
        .builder().username(USERNAME).password(PASSWORD).registrationCode(REGISTRATION_CODE).build();

    restTemplate.exchange(getRegistrationUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    final ResponseEntity<String> loginResponse = restTemplate.exchange(getLoginUri(USERNAME, PASSWORD), HttpMethod.POST, null, String.class);
    final String session = loginResponse.getHeaders().getFirst(HttpHeaders.SET_COOKIE);

    HttpHeaders headers = new HttpHeaders();
    headers.add("Cookie", session);

    Collection<Book> books = response.getBody();
    books.stream().forEach(book -> {
      restTemplate.exchange(getBookUri(book.getId()), HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class);
    });

    response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }

  @Test
  public void cacheWorking() {
    ResponseEntity<Collection<Book>> response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    Mockito.verify(bookService, Mockito.times(1)).getAllBooks();
  }
}
