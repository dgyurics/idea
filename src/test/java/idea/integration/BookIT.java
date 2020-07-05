package idea.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import idea.model.entity.Book;
import idea.model.dto.BookRequestModel;
import idea.model.dto.UserRequestModel;
import java.util.Collection;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.ApplicationContext;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class BookIT extends BaseIT {
  private static final String USERNAME_ADMIN = "lagom3922@gmail.com";
  private static final String PASSWORD_ADMIN = "password123";

  @Autowired
  ApplicationContext context;

  @Before
  public void before() throws Exception {
    CommandLineRunner runner = context.getBean(CommandLineRunner.class);
    runner.run( USERNAME_ADMIN, PASSWORD_ADMIN);
  }

  @Test
  public void getBooks() {
    ResponseEntity<Collection<Book>> response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  public void uploadBook() {
    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    final ResponseEntity<String> response = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    assertNotNull(response);
    assertEquals(HttpStatus.OK, response.getStatusCode());

    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + response.getBody());

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
  public void removeBook() {
    ResponseEntity<Collection<Book>> response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertTrue(response.getBody().size() >= 5);

    UserRequestModel model = UserRequestModel.builder()
        .username(USERNAME_ADMIN).password(PASSWORD_ADMIN).build();
    final ResponseEntity<String> responseLogin = restTemplate.exchange(getLoginUri(), HttpMethod.POST, new HttpEntity<>(model), String.class);
    HttpHeaders headers = new HttpHeaders();
    headers.add("Authorization", "Bearer " + responseLogin.getBody());

    Collection<Book> books = response.getBody();
    books.stream().forEach(book -> {
      restTemplate.exchange(getBookUri(book.getId()), HttpMethod.DELETE, new HttpEntity<>(null, headers), String.class);
    });

    response = restTemplate.exchange(getBookUri(), HttpMethod.GET, null, new ParameterizedTypeReference<Collection<Book>>(){});
    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertNotNull(response.getBody());
    assertEquals(0, response.getBody().size());
  }
}
