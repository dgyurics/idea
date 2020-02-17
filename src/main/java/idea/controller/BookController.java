package idea.controller;

import idea.model.entity.Book;
import idea.model.dto.BookRequestModel;
import idea.service.BookService;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("book")
@RequiredArgsConstructor
public class BookController {
  private final BookService service;

  @GetMapping
  public Collection<Book> getBooks() {
    return service.getAllBooks();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public Book createBook(@RequestBody BookRequestModel book) {
    return service.createBook(book);
  }

  @ResponseStatus(HttpStatus.OK)
  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void deleteBook(@PathVariable long id) {
    service.removeBook(id);
  }
}
