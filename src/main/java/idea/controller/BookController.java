package idea.controller;

import idea.model.entity.Book;
import idea.model.request.BookRequestModel;
import idea.repository.BookRepository;
import java.util.Collection;
import org.modelmapper.ModelMapper;
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
public class BookController {
  private final BookRepository repository;
  private final ModelMapper mapper;

  public BookController(BookRepository repository, ModelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @GetMapping
  public Collection<Book> getBooks() {
    return repository.findAll();
  }

  @ResponseStatus(HttpStatus.CREATED)
  @PutMapping
  @PreAuthorize("hasRole('ROLE_USER')")
  public Book createBook(@RequestBody BookRequestModel bookDto) {
    Book book = new Book();
    mapper.map(bookDto, book);
    return repository.save(book);
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ROLE_USER')")
  public void deleteBook(@PathVariable long id) {
    repository.deleteById(id);
  }
}
