package com.dodecaedro.library.repository;

import com.dodecaedro.library.data.pojo.Book;
import org.springframework.data.repository.RepositoryDefinition;

import java.util.List;

@RepositoryDefinition(domainClass = Book.class, idClass = Integer.class)
public interface BookRepository {
  Book findOne(Integer bookId);
  Book save(Book book);
  Book findByIsbn(String isbn);
  List<Book> findAll();
}
