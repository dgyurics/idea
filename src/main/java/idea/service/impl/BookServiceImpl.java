package idea.service.impl;

import idea.model.entity.Book;
import idea.model.request.BookRequestModel;
import idea.repository.BookRepository;
import idea.service.BookService;
import java.util.Collection;
import org.modelmapper.ModelMapper;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
public class BookServiceImpl implements BookService {
  private final BookRepository repository;
  private final ModelMapper mapper;

  BookServiceImpl(BookRepository repository, ModelMapper mapper) {
    this.repository = repository;
    this.mapper = mapper;
  }

  @Cacheable(value="bookCache")
  @Override
  public Collection<Book> getAllBooks() {
    return repository.findAll();
  }

  @CacheEvict(value="bookCache", allEntries=true)
  @Override
  public Book createBook(BookRequestModel bookDto) {
    Book book = new Book();
    mapper.map(bookDto, book);
    return repository.save(book);
  }

  @CacheEvict(value="bookCache", allEntries=true)
  @Override
  public void removeBook(Long id) {
    repository.deleteById(id);
  }
}
