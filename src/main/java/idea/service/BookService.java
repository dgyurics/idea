package idea.service;

import idea.model.entity.Book;
import idea.model.request.BookRequestModel;
import java.util.Collection;

public interface BookService {
  Collection<Book> getAllBooks();
  Book createBook(BookRequestModel book);
  void removeBook(Long id);
}
